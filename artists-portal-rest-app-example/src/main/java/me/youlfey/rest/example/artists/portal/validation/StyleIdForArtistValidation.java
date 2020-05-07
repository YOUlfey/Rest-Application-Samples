package me.youlfey.rest.example.artists.portal.validation;

import me.youlfey.rest.example.artists.portal.domain.Artist;
import me.youlfey.rest.example.artists.portal.validation.annotation.ArtistStyleIdValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class StyleIdForArtistValidation implements ConstraintValidator<ArtistStyleIdValid, Artist> {
    @Override
    public boolean isValid(@NotNull Artist artist, ConstraintValidatorContext constraintValidatorContext) {
        return Objects.nonNull(artist)
                && Objects.nonNull(artist.getStyle())
                && Objects.nonNull(artist.getStyle().getId());
    }
}
