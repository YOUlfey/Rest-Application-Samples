package me.youlfey.rest.example.news.portal.utils;

import me.youlfey.rest.example.news.portal.domain.jms.LogOperation;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("jms")
public class LogTransformer {

    private final ObjectMapper objectMapper;

    public LogTransformer() {
        this.objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public <T> LogOperation transform(T obj) {
        try {
            return LogOperation.builder()
                    .message(objectMapper.writeValueAsString(obj))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }
}
