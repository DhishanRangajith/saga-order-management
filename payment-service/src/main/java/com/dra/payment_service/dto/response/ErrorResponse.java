package com.dra.payment_service.dto.response;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private Map<String, Object> data;

    public ErrorResponse(String message){
        this.message = message;
    }

}
