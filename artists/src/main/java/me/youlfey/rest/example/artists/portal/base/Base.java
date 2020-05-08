package me.youlfey.rest.example.artists.portal.base;

import java.util.UUID;

public interface Base<T extends Base> {
    UUID getId();

    void update(T updateRequest);
}
