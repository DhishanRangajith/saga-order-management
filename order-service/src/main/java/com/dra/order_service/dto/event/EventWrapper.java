package com.dra.order_service.dto.event;

import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EventWrapper {
    private String eventType;
    private Object data;
    private Instant timestamp;

    public EventWrapper(String eventType, Object data){
        this.timestamp = Instant.now();
        this.eventType = eventType;
        this.data = data;
    }

}
