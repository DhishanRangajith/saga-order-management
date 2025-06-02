package com.dra.order_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dra.order_service.dto.request.OrderCreateData;
import com.dra.order_service.dto.request.OrderSearchData;
import com.dra.order_service.dto.response.OrderData;
import com.dra.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.Map;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<OrderData>> getList(
        @RequestParam(required = false) Long orderId,
        @RequestParam(required = false) String productCode,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer pageSize
    ) {
        OrderSearchData orderSearchData = OrderSearchData.builder()
                                                    .id(orderId)
                                                    .productCode(productCode)
                                                    .status(status)
                                                    .page(page==null?0:page-1)
                                                    .pageSize(pageSize==null?Integer.MAX_VALUE:pageSize)
                                                    .build();
        Page<OrderData> data = this.orderService.getOrderList(orderSearchData);
        return ResponseEntity.ok(data);
    }

    @GetMapping("{id}")
    public ResponseEntity<OrderData> getOrder(@PathVariable Long id) {
        OrderData data = this.orderService.getOrder(id);
        return ResponseEntity.ok(data);
    }

    @PostMapping
    public ResponseEntity<OrderData> createOrder(@RequestBody @Valid OrderCreateData orderCreateData) {
        OrderData data = this.orderService.createOrder(orderCreateData);
        return new ResponseEntity<>(data, HttpStatus.CREATED);
    }

    @PatchMapping("{id}/cancel")
    public ResponseEntity<Map<String, Object>> cancelOrder(@PathVariable Long id) {
        boolean status = this.orderService.cancelOrder(id);
        Map<String, Object> data = Map.of("message","Accepted", "status",status);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(data);
    }

}
