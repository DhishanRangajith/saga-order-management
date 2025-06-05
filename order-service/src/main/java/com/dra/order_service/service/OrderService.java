package com.dra.order_service.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.dra.order_service.dto.publisherEvent.OrderCancelEventData;
import com.dra.order_service.dto.publisherEvent.OrderCreateEventData;
import com.dra.order_service.dto.request.OrderCreateData;
import com.dra.order_service.dto.request.OrderSearchData;
import com.dra.order_service.dto.request.ProductCreateData;
import com.dra.order_service.dto.response.OrderData;
import com.dra.order_service.entity.OrderEntity;
import com.dra.order_service.entity.OrderProductEntity;
import com.dra.order_service.entity.OrderProductId;
import com.dra.order_service.enums.OrderStatus;
import com.dra.order_service.exception.BadException;
import com.dra.order_service.exception.NotFoundException;
import com.dra.order_service.mapper.OrderMapper;
import com.dra.order_service.repository.OrderProductRepository;
import com.dra.order_service.repository.OrderRepository;
import com.dra.order_service.specification.OrderSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final OrderMapper orderMapper;
    private final KafkaEventProcessor kafkaEventProcessor;

    public OrderEntity getOrderEntity(Long id){
        OrderEntity orderEntity = this.orderRepository.findById(id)
                                                    .orElseThrow(() -> new NotFoundException("Order is not found."));
       return orderEntity;
    }

    public OrderData getOrder(Long id){
        OrderEntity orderEntity = this.getOrderEntity(id);
        OrderData orderData = this.orderMapper.toOrderData(orderEntity);
        return orderData;
    }

    public Page<OrderData> getOrderList(OrderSearchData orderSearchData){
        Specification<OrderEntity> specification = Specification.allOf(
            OrderSpecification.hasId(orderSearchData.getId()),
            OrderSpecification.hasProductCode(orderSearchData.getProductCode()),
            OrderSpecification.hasStatus(orderSearchData.getStatus())
        );
        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(orderSearchData.getPage(), orderSearchData.getPageSize(), sort);
        Page<OrderEntity> pagedEntityList = this.orderRepository.findAll(specification, pageable);
        List<OrderData> orderList = this.orderMapper.toOrderDataList(pagedEntityList.getContent());
        Page<OrderData> pagedDataList = new PageImpl<>(orderList, pageable, pagedEntityList.getTotalElements());
        return pagedDataList;
    }

    @Transactional
    public OrderData createOrder(OrderCreateData orderCreateData){

        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStatus(OrderStatus.PROCESSING);
        Double amount = 0.0;
        OrderEntity savedOrderEntity = this.orderRepository.save(orderEntity);
        savedOrderEntity.setOrderProducts(new ArrayList<>());

        List<ProductCreateData> productList = orderCreateData.getProducts();
        for(ProductCreateData product : productList){
            OrderProductId orderProductId = new OrderProductId(savedOrderEntity.getId(),product.getProductCode());
            OrderProductEntity orderProductEntity = new OrderProductEntity();
            orderProductEntity.setOrder(savedOrderEntity);
            orderProductEntity.setOrderProductId(orderProductId);
            orderProductEntity.setPrice(product.getPrice());
            orderProductEntity.setProductName(product.getName());
            orderProductEntity.setQuantity(product.getQuantity());
            OrderProductEntity savedEntity = this.orderProductRepository.save(orderProductEntity);
            orderEntity.getOrderProducts().add(savedEntity);
            amount += product.getPrice()*product.getQuantity();
        }
        orderEntity.setAmount(amount);
        savedOrderEntity = this.orderRepository.save(orderEntity);
        OrderData orderData = this.orderMapper.toOrderData(savedOrderEntity);
        OrderCreateEventData orderCreateEventData = this.orderMapper.toOrderCreateEvent(savedOrderEntity);
        this.kafkaEventProcessor.publishOrderCreatedEvent(orderCreateEventData);
        return orderData;
    }

    @Transactional
    public boolean cancelOrder(Long id){

        OrderEntity orderEntity = this.getOrderEntity(id);
        List<OrderStatus> cancelEligibleStatusList = List.of(
            OrderStatus.INVENTORY_RESERVED_PENDING_PAYMENT,
            OrderStatus.INVENTORY_UNAVAILABLE,
            OrderStatus.PROCESSING,
            OrderStatus.INVALID_DATA
        );
        if(!cancelEligibleStatusList.contains(orderEntity.getStatus())) 
            throw new BadException("Order is not eligible to cancel.");
              
        orderEntity.setStatus(OrderStatus.CANCELLED);
        this.orderRepository.save(orderEntity);
        OrderCancelEventData orderCancelEvent = this.orderMapper.tOrderCancelEvent(orderEntity);
        this.kafkaEventProcessor.publishOrderCancelledEvent(orderCancelEvent);
        return true;
    }

    @Transactional
    public boolean changeStatus(Long id, OrderStatus newStatus){
        OrderEntity orderEntity = this.getOrderEntity(id);
        boolean checkToUpdate = false;
        switch (orderEntity.getStatus()) {
            case INVALID_DATA,INVENTORY_UNAVAILABLE:
                checkToUpdate = List.of(OrderStatus.CANCELLED).contains(newStatus);
                break;
            case INVENTORY_RESERVED_PENDING_PAYMENT:
                checkToUpdate = List.of(
                    OrderStatus.CANCELLED,
                    OrderStatus.INVENTORY_RESERVED_PAYMENT_COMPLETED, 
                    OrderStatus.CLOSED_DELIVERED).contains(newStatus);
                break;
            case INVENTORY_RESERVED_PAYMENT_COMPLETED:
                checkToUpdate = List.of(OrderStatus.CLOSED_DELIVERED).contains(newStatus);
                break;
            default:
                checkToUpdate = false;
                break;
        }

        if(checkToUpdate){
            orderEntity.setStatus(newStatus);
            this.orderRepository.save(orderEntity);
        }else{
            throw new BadException("Order is not eligible to update with provided status.");
        }
        return true;
    }

}
