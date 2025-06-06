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
import com.dra.order_service.dto.event.publisherEvent.OrderCancelEventData;
import com.dra.order_service.dto.event.publisherEvent.OrderCreateEventData;
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
        orderEntity.setStatus(OrderStatus.CREATION_PROCESSING);
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
            OrderStatus.CREATION_SUCCESS,
            OrderStatus.CREATION_PROCESSING
        );
        if(!cancelEligibleStatusList.contains(orderEntity.getStatus())) 
            throw new BadException("Order is not eligible to cancel.");
              
        orderEntity.setStatus(OrderStatus.CANCELLATION_PROCESSING);
        this.orderRepository.save(orderEntity);
        OrderCancelEventData orderCancelEvent = this.orderMapper.toOrderCancelEvent(orderEntity);
        this.kafkaEventProcessor.publishOrderCancelledEvent(orderCancelEvent);
        return true;
    }

    @Transactional
    public boolean changeStatusByEvents(Long id, OrderStatus newStatus, String subStatus){
        OrderEntity orderEntity = this.getOrderEntity(id);
        boolean valid = false;
        if(newStatus == null){
            valid = List.of(OrderStatus.CREATION_PROCESSING, OrderStatus.CREATION_SUCCESS)
                                        .contains(orderEntity.getStatus());
        }else{
            switch (newStatus) {
                case CREATION_SUCCESS:
                case CREATION_FAILED:
                    valid = orderEntity.getStatus().equals(OrderStatus.CREATION_PROCESSING);
                    break;

                case CANCELLATION_SUCCESS:
                case CANCELLATION_FAILED:
                    valid = orderEntity.getStatus().equals(OrderStatus.CANCELLATION_PROCESSING);
                    break;
                default: break;
            }
        }

        if(!valid)
            // throw new BadException("Order is not eligible to update with provided status.");
            return false;

        if(newStatus != null) orderEntity.setStatus(newStatus);
        orderEntity.setSubStatus(subStatus);
        this.orderRepository.save(orderEntity);
        return true;
    }

}
