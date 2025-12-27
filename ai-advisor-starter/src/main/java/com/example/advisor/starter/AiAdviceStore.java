package com.example.advisor.starter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AiAdviceStore {
    private final Map<String, String> map = new ConcurrentHashMap<>();
    public void put(String id, String advice) { map.put(id, advice); }
    public String get(String id) { return map.get(id); }
}
