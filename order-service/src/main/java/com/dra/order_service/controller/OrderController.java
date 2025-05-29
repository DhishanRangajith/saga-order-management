package com.dra.order_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dra.order_service.dto.OrderData;
import com.dra.order_service.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderData>> getList() {
        List<OrderData> data = this.orderService.getOrderList();
        return ResponseEntity.ok(data);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderData> getOrder(@PathVariable Long id) {
        OrderData data = this.orderService.getOrder(id);
        return ResponseEntity.ok(data);
    }

    @PostMapping
    public ResponseEntity<OrderData> createOrder(@RequestBody @Valid OrderData orderDto) {
        OrderData data = this.orderService.createOrder(orderDto);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PatchMapping("{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Long id) {
        boolean status = this.orderService.cancelOrder(id);
        Map<String, Object> data = Map.of("message","Accepted", "status",status);
        return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
    }

}
