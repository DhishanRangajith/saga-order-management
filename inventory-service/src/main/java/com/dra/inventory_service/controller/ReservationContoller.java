package com.dra.inventory_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dra.inventory_service.dto.request.CancelReservationData;
import com.dra.inventory_service.dto.request.ReservationCreateData;
import com.dra.inventory_service.dto.request.ReservationSearchData;
import com.dra.inventory_service.dto.response.ReservationData;
import com.dra.inventory_service.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/reservations")
@RequiredArgsConstructor
@Validated
public class ReservationContoller {

    @Value("${spring.appData.pageMaxSize}")
    private int pageMaxSize;

    private final ReservationService reservationService;

    @GetMapping("orders/{orderId}")
    public ResponseEntity<ReservationData> getReservation(@PathVariable Long orderId){
        ReservationData reservationData = this.reservationService.getReservationByOrderId(orderId);
        return ResponseEntity.ok().body(reservationData);
    }

    @GetMapping
    public ResponseEntity<Page<ReservationData>> searchReservations(
        @RequestParam(required = false) Long orderId,
        @RequestParam(required = false) String productCode,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer pageSize
    ){
        ReservationSearchData reservationSearchData = ReservationSearchData.builder()
                                                        .orderId(orderId)
                                                        .productCode(productCode)
                                                        .status(status)
                                                        .page(page==null?0:page-1)
                                                        .pageSize(pageSize==null?pageMaxSize:pageSize)
                                                        .build();

        Page<ReservationData> pagedData = this.reservationService.searchReservations(reservationSearchData);
        return ResponseEntity.ok().body(pagedData);
    }

    @PostMapping
    public ResponseEntity<ReservationData> createReservation(@RequestBody @Valid ReservationCreateData reservationCreateData){
        ReservationData reservationData = this.reservationService.createReservation(reservationCreateData);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationData);
    }

    @PatchMapping("orders/{orderId}/cancel")
    public ResponseEntity<ReservationData> cancelReservation(@PathVariable Long orderId, @RequestBody CancelReservationData cancelReservationData){
        ReservationData reservationData = this.reservationService.cancelReservation(orderId, cancelReservationData);
        return ResponseEntity.ok().body(reservationData);
    }

}
