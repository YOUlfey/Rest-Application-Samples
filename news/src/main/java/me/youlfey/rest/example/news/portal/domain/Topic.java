package me.youlfey.rest.example.news.portal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;

    @NotNull
    @Size(min = 3, max = 50)
    private String name;

    @UpdateTimestamp
    private LocalDateTime modifiedDate;

    public static Topic copyDetached(Topic topic) {
        return Topic.builder()
                .id(topic.getId())
                .name(topic.getName())
                .modifiedDate(topic.getModifiedDate())
                .build();
    }
}
