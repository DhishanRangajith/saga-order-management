package com.dra.order_service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.dra.order_service.dto.OrderData;

@Aspect
@Component
public class OrderAspect {

    @Before("@annotation(com.dra.order_service.annotation.CreateOrderRequestDataValidation)")
    public void createOrderValidation(JoinPoint joinPoint){
        // Object[] args = joinPoint.getArgs();
        // for (Object arg : args) {
        //     if (arg instanceof OrderData orderData) {
        //         if (orderData.getId() != null) {
        //             throw new IllegalArgumentException("Order ID is not required");
        //         }else if(orderData.getQuantity() == 0){
        //             throw new IllegalArgumentException("Quantity should be grater than 0");
        //         }else if(orderData.getProducts() == null){
        //             throw new IllegalArgumentException("Products are required");
        //         }
        //     }
        // }
    }

}
