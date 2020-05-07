package me.youlfey.rest.example.artists.portal.domain;

import me.youlfey.rest.example.artists.portal.base.Base;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Style implements Base<Style> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @ApiModelProperty(notes = "Identifier", required = true)
    private UUID id;

    @NotNull
    @ApiModelProperty(notes = "Style name", required = true)
    private String name;

    @ApiModelProperty(notes = "Style description")
    private String description;

    @Override
    public void update(Style updateRequest) {
        if (Objects.nonNull(updateRequest.name)) {
            this.name = updateRequest.name;
        }
        if (Objects.nonNull(updateRequest.description)) {
            this.description = updateRequest.description;
        }
    }
}
