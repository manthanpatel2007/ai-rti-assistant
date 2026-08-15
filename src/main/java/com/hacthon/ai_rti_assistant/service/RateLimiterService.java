package com.hacthon.ai_rti_assistant.service;

import com.hacthon.ai_rti_assistant.entity.RequestTracker;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<String, RequestTracker> requestMap = new ConcurrentHashMap<>();

    public boolean isAllowed(String key, int maxRequests, long windowMillis) {
        long now = System.currentTimeMillis();

        RequestTracker tracker = requestMap.compute(key, (k, existing) -> {
            if (existing == null || now - existing.getWindowStart() > windowMillis) {
                return new RequestTracker(now); // naya window start
            } else {
                existing.setCount(existing.getCount() + 1);
                return existing;
            }
        });

        return tracker.getCount() <= maxRequests;
    }
}