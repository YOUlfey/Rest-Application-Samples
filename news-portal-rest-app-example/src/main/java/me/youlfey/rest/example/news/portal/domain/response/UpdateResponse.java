package me.youlfey.rest.example.news.portal.domain.response;

import me.youlfey.rest.example.news.portal.domain.internal.Delta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateResponse<T> {
    private boolean isUpdated = false;
    private Delta<T> delta;
}
