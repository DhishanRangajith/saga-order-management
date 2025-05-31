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
import com.dra.inventory_service.dto.request.OrderCreateData;
import com.dra.inventory_service.dto.request.OrderSearchData;
import com.dra.inventory_service.dto.response.OrderData;
import com.dra.inventory_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
@Validated
public class OrderContoller {

    @Value("${spring.appData.pageMaxSize}")
    private int pageMaxSize;

    private final OrderService orderService;

    @GetMapping("{orderId}")
    public ResponseEntity<OrderData> getOrder(@PathVariable Long orderId){
        OrderData orderData = this.orderService.getOrder(orderId);
        return ResponseEntity.ok().body(orderData);
    }

    @GetMapping
    public ResponseEntity<Page<OrderData>> searchOrders(
        @RequestParam(required = false) Long orderId,
        @RequestParam(required = false) String productCode,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer pageSize
    ){
        OrderSearchData orderSearchData = OrderSearchData.builder()
                                                        .orderId(orderId)
                                                        .productCode(productCode)
                                                        .status(status)
                                                        .page(page==null?0:page-1)
                                                        .pageSize(pageSize==null?pageMaxSize:pageSize)
                                                        .build();

        Page<OrderData> pagedData = this.orderService.getOrdersList(orderSearchData);
        return ResponseEntity.ok().body(pagedData);
    }

    @PostMapping
    public ResponseEntity<OrderData> createOrder(@RequestBody @Valid OrderCreateData orderCreateData){
        OrderData orderData = this.orderService.createOrder(orderCreateData);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderData);
    }

    @PatchMapping("{orderId}/cancel")
    public ResponseEntity<OrderData> cancelOrder(@RequestBody CancelReservationData cancelReservationData){
        OrderData orderData = this.orderService.cancelOrder(cancelReservationData);
        return ResponseEntity.ok().body(orderData);
    }

}
