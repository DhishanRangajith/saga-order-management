package com.dra.inventory_service.extension;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String message){
        super(message);
    }

}
