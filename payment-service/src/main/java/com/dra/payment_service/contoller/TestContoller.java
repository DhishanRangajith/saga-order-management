package com.dra.payment_service.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class TestContoller {


    private final KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("test")
    public String test() {


        this.kafkaTemplate.send("SHANTHA", "TANIYAAAAAAAAAAAAAAAAAAAAAAAA");

        return "TESTING...";
    }


    public void aaa(){

    }
    

}
