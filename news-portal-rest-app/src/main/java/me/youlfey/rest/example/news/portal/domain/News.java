package me.youlfey.rest.example.news.portal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Size(min = 30, max = 200)
    @NotNull
    private String message;
    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    public static News copyDetached(News news) {
        return News.builder()
                .id(news.getId())
                .message(news.getMessage())
                .modifiedDate(news.getModifiedDate())
                .topic(Topic.copyDetached(news.getTopic()))
                .build();
    }
}