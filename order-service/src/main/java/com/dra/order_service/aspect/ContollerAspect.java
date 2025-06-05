package com.dra.order_service.aspect;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class ContollerAspect {

    private static final Logger log = LoggerFactory.getLogger(ContollerAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object loggingContollers(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();

        Map<String, Object> data = new LinkedHashMap<>();

        // 1. Log Path Variables and Request Params
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, Object> params = new LinkedHashMap<>();
        parameterMap.forEach((k, v) -> {
            if (v != null && v.length == 1) {
                params.put(k, v[0]);
            } else {
                params.put(k, v);
            }
        });
        data.put("requestParams", params);

        // 2. Log HTTP Method and URL
        data.put("method", request.getMethod());
        data.put("url", request.getRequestURI());

        // 3. Try to get Path Variables from URI template variables (if available)
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute("org.springframework.web.servlet.HandlerMapping.uriTemplateVariables");
        if (pathVariables != null) {
            data.put("pathVariables", pathVariables);
        }

        // 4. Log method arguments (e.g. request body) as JSON
        try {
            Object[] args = joinPoint.getArgs();
            data.put("methodArgs", args);
        } catch (Exception e) {
            data.put("methodArgs", "Error getting args");
        }

        String requestDataJson = objectMapper.writeValueAsString(data);
        System.out.println("REQUEST DATA: " + requestDataJson);

        // Proceed and get the response
        Object result = joinPoint.proceed();

        // Log response output as JSON
        String responseJson;
        try {
            responseJson = objectMapper.writeValueAsString(result);
        } catch (Exception e) {
            responseJson = "Cannot convert response to JSON";
        }
        System.out.println("RESPONSE DATA: " + responseJson);

        return result;
    }

}
