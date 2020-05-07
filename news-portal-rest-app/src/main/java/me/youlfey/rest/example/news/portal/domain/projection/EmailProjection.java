package me.youlfey.rest.example.news.portal.domain.projection;

import org.springframework.beans.factory.annotation.Value;

public interface EmailProjection {
    @Value("#{target.email}")
    String getEmail();
}
