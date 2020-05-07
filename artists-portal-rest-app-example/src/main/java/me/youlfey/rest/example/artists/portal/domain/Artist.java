package me.youlfey.rest.example.artists.portal.domain;

import me.youlfey.rest.example.artists.portal.base.Base;
import me.youlfey.rest.example.artists.portal.validation.annotation.ArtistDateValid;
import me.youlfey.rest.example.artists.portal.validation.annotation.ArtistStyleIdValid;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@ArtistDateValid(message = "Death date should be more than birth date")
@ArtistStyleIdValid(message = "style id cannot be null")
public class Artist implements Base<Artist> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @ApiModelProperty(notes = "Identifier", required = true)
    private UUID id;

    @NotNull
    @ApiModelProperty(notes = "Artist name", required = true)
    private String name;

    @NotNull
    @ApiModelProperty(notes = "Date of birth", required = true)
    private LocalDate dateBirth;

    @ApiModelProperty(notes = "Date of death")
    private LocalDate dateDeath;

    @ManyToOne
    @JoinColumn(name = "style")
    @ApiModelProperty(notes = "Artist style")
    private Style style;

    @Override
    public void update(Artist updateRequest) {
        if (Objects.nonNull(updateRequest.name)) {
            this.name = updateRequest.name;
        }
        if (Objects.nonNull(updateRequest.dateDeath)) {
            this.dateDeath = updateRequest.dateDeath;
        }
        if (Objects.nonNull(updateRequest.dateBirth)) {
            this.dateBirth = updateRequest.dateBirth;
        }
        if (Objects.nonNull(updateRequest.style)) {
            this.style = updateRequest.style;
        }
    }
}
