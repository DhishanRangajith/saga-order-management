package com.dra.payment_service.contoller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dra.payment_service.dto.request.PaymentSearchData;
import com.dra.payment_service.dto.request.PaymentUpdateData;
import com.dra.payment_service.dto.response.PaymentData;
import com.dra.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Value("${app.pageMaxSize}")
    private int maxPageSize;

    private final PaymentService paymentService;

    @GetMapping("orders/{orderId}")
    public ResponseEntity<PaymentData> getPaymentByOrderId(@PathVariable Long orderId){
        PaymentData paymentData = this.paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok().body(paymentData);
    }

    @GetMapping
    public ResponseEntity<Page<PaymentData>> seachPayments(
        @RequestParam(required = false) Long orderId,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer pageSize
    ){
        PaymentSearchData paymentSearchData = PaymentSearchData.builder()
                                                            .orderId(orderId)
                                                            .status(status)
                                                            .page(page==null?0:page-1)
                                                            .pageSize(pageSize==null?maxPageSize:pageSize)
                                                            .build();
        Page<PaymentData> pagedData = this.paymentService.getPaymentList(paymentSearchData);
        return ResponseEntity.ok().body(pagedData);
    }

    @PatchMapping("orders/{orderId}/status")
    public ResponseEntity<PaymentData> updatePayment(@PathVariable Long orderId, @RequestBody PaymentUpdateData paymentCreateData){
        PaymentData paymentData = this.paymentService.updatePaymentStatus(orderId, paymentCreateData, false);
        return ResponseEntity.ok().body(paymentData);
    }

}
