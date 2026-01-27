package com.example.demo.infrastructure.utils;

import org.springframework.stereotype.Component;

@Component
public class JsonUtil {
    public final tools.jackson.databind.ObjectMapper mapper = new tools.jackson.databind.ObjectMapper();
}