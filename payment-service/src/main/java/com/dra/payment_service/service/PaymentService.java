package com.dra.payment_service.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.dra.payment_service.dto.event.publisher.PaymentEventData;
import com.dra.payment_service.dto.request.PaymentCreateData;
import com.dra.payment_service.dto.request.PaymentSearchData;
import com.dra.payment_service.dto.request.PaymentUpdateData;
import com.dra.payment_service.dto.response.PaymentData;
import com.dra.payment_service.entity.PaymentEntity;
import com.dra.payment_service.enums.PaymentStatus;
import com.dra.payment_service.exception.BadException;
import com.dra.payment_service.exception.NotFoundException;
import com.dra.payment_service.mapper.PaymentMapper;
import com.dra.payment_service.repository.PaymentRepository;
import com.dra.payment_service.repository.specification.PaymentSpecification;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final KafkaEventProcessor kafkaEventProcessor;

    public PaymentEntity getPaymentEntityByOrderId(Long orderId){
        return this.paymentRepository.findByOrderId(orderId)
                                    .orElseThrow(() -> new NotFoundException("Payment is not found."));
    }

    public PaymentData getPaymentByOrderId(Long orderId){
        PaymentEntity paymentEntity = this.getPaymentEntityByOrderId(orderId);
        PaymentData paymentData = this.paymentMapper.toDto(paymentEntity);
        return paymentData;
    }

    public Page<PaymentData> getPaymentList(PaymentSearchData paymentSearchData){
        Specification<PaymentEntity> specifications = Specification.allOf(
            PaymentSpecification.hasOrder(paymentSearchData.getOrderId()),
            PaymentSpecification.hasStatus(paymentSearchData.getStatus())
        );

        Sort sort = Sort.by("updatedAt").descending();
        Pageable pageable = PageRequest.of(paymentSearchData.getPage(), paymentSearchData.getPageSize(), sort);
        Page<PaymentEntity> pagedEntityList = this.paymentRepository.findAll(specifications, pageable);
        List<PaymentData> paymentDataList = this.paymentMapper.toDtoList(pagedEntityList.getContent());
        Page<PaymentData> pagedDataList = new PageImpl<>(paymentDataList, pageable, pagedEntityList.getTotalElements());
        return pagedDataList;
    }

    @Transactional
    public PaymentData createPayment(@Valid PaymentCreateData paymentCreateData){
        PaymentEntity paymentEntity = this.paymentMapper.toEntity(paymentCreateData);
        paymentEntity.setStatus(PaymentStatus.PENDING);
        PaymentEntity savedPaymentEntity = this.paymentRepository.save(paymentEntity);
        PaymentData paymentData = this.paymentMapper.toDto(savedPaymentEntity);
        return paymentData;
    }

    @Transactional
    public PaymentData updatePaymentStatus(Long orderId, @Valid PaymentUpdateData paymentUpdateData, boolean event){
        PaymentEntity paymentEntity = this.getPaymentEntityByOrderId(orderId);
        boolean valid = false;
        switch (paymentEntity.getStatus()) {
            case PENDING:
                valid = List.of(
                    PaymentStatus.CANCELLED_ORDER_CANCELLED, 
                    PaymentStatus.COMPLETED,
                    PaymentStatus.FAILED
                ).contains(paymentUpdateData.getStatus());
                break;
            case COMPLETED:
                valid = List.of(
                    PaymentStatus.CANCELLED_ORDER_CANCELLED, PaymentStatus.CANCELLED_ORDER_CANCELLED_REFUND
                ).contains(paymentUpdateData.getStatus());
                if(valid) paymentUpdateData.setStatus(PaymentStatus.CANCELLED_ORDER_CANCELLED_REFUND);
            default:
                break;
        }

        if(!valid){
            if(!event) throw new BadException("Invalid status transition. Cannot update payment status to '" + paymentUpdateData.getStatus().name() + "'.");
            else return null;
        }

        paymentEntity.setStatus(paymentUpdateData.getStatus());
        PaymentEntity savedPaymentEntity = this.paymentRepository.save(paymentEntity);
        PaymentData paymentData = this.paymentMapper.toDto(savedPaymentEntity);


        //publish event
        switch (savedPaymentEntity.getStatus()) {
            case COMPLETED:
                this.kafkaEventProcessor.publishPaymentSuccessEvent(new PaymentEventData(orderId));
                break;
            case CANCELLED_ORDER_CANCELLED: 
                this.kafkaEventProcessor.publishPaymentCancelledEvent(new PaymentEventData(orderId));
                break;
            case FAILED: 
                this.kafkaEventProcessor.publishPaymentFailedEvent(new PaymentEventData(orderId));
                break;
            default:
                break;
        }
        
        return paymentData;
    }

}
