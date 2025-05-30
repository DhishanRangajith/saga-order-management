package com.dra.inventory_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dra.inventory_service.dto.InventoryReservationData;
import com.dra.inventory_service.dto.request.ReservationSearchData;
import com.dra.inventory_service.service.InventoryReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/v1/reservation")
@RequiredArgsConstructor
public class InventoryReservationController {

    @Value("${spring.appData.pageMaxSize}")
    private int pageMaxSize;

    private final InventoryReservationService reservationService;

    @GetMapping
    public ResponseEntity<Page<InventoryReservationData>> getReservationList(
        @RequestParam(required = false) Long orderId,
        @RequestParam(required = false) String productCode,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer pageSize
    ) {
        ReservationSearchData reservationSearchData = ReservationSearchData.builder()
                                                                    .orderId(orderId)
                                                                    .productCode(productCode)
                                                                    .status(status)
                                                                    .page(page==null?0:page-1)
                                                                    .pageSize(pageSize==null?pageMaxSize:pageSize)
                                                                    .build();
        Page<InventoryReservationData> pageData = this.reservationService.getReservationList(reservationSearchData);           
        return ResponseEntity.status(HttpStatus.OK).body(pageData);
    }
    

}
