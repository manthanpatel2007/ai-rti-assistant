package com.hacthon.ai_rti_assistant.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestTracker {
    private int count;
    private long windowStart;

    public RequestTracker(long windowStart) {
        this.count = 1;
        this.windowStart = windowStart;
    }

}