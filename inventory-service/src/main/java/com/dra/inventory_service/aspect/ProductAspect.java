package com.dra.inventory_service.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ProductAspect {

    @Before("@annotation(com.dra.inventory_service.annotation.ProductCreateValidation)")
    public void validateProductCreationData(JoinPoint joinPoint){

    }

    @Before("@annotation(com.dra.inventory_service.annotation.ProductUpdateValidation)")
    public void validateProductUpdateData(JoinPoint joinPoint){

    }

}
