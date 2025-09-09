package com.microblogging.timelines.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    public static ObjectMapper buildObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Register support for Java 8 date/time (Instant, LocalDateTime, etc.)
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        // Write dates as ISO-8601 instead of timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // Optional: ignore unknown fields if producer adds new props
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return buildObjectMapper();
    }

}