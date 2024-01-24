package com.project.ams.vaadin.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // Configure Jackson to handle Java 8 date/time types
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String convertToJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            // Handle the exception according to your requirements
            e.printStackTrace();
            return null;
        }
    }
}
