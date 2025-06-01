package com.dra.payment_service.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }

}
