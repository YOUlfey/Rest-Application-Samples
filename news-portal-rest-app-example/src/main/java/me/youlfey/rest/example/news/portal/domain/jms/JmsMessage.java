package me.youlfey.rest.example.news.portal.domain.jms;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface JmsMessage {
    Class getType();

    @JsonIgnore
    default void setType(Class type) {

    }
}
