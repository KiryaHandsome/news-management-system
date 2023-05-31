package ru.clevertec.newssystem.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    /**
     * Bean for serializing LocalDateTime in format {@code yyyy-MM-dd HH:mm:ss}
     */
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTimeSerializer localDateSerializer = new LocalDateTimeSerializer(pattern);
        javaTimeModule.addSerializer(LocalDateTime.class, localDateSerializer);
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }
}
