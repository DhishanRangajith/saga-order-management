package com.dra.payment_service.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.dra.payment_service.dto.event.consumer.ReservationSuccessEventData;
import com.dra.payment_service.dto.request.PaymentCreateData;
import com.dra.payment_service.dto.response.PaymentData;
import com.dra.payment_service.entity.PaymentEntity;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    PaymentEntity toEntity(PaymentCreateData paymentCreateData);

    PaymentData toDto(PaymentEntity paymentEntity);
    List<PaymentData> toDtoList(List<PaymentEntity> paymentEntityList);

    PaymentCreateData toPaymentCreateData(ReservationSuccessEventData reservationEventData);

}
