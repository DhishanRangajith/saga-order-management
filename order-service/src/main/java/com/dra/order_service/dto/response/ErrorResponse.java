package com.dra.order_service.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    // private int code;
    private String message;
    private Map<String, Object> data;

    public ErrorResponse(String message){
        this.message = message;
    }

}
