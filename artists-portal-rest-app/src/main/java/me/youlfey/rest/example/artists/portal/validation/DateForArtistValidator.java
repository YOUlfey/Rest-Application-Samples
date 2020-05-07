package me.youlfey.rest.example.artists.portal.validation;

import me.youlfey.rest.example.artists.portal.domain.Artist;
import me.youlfey.rest.example.artists.portal.validation.annotation.ArtistDateValid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public class DateForArtistValidator implements ConstraintValidator<ArtistDateValid, Artist> {
    @Override
    public boolean isValid(@NotNull Artist artist, ConstraintValidatorContext constraintValidatorContext) {
        final LocalDate dateDeath = artist.getDateDeath();
        if (Objects.isNull(dateDeath)) {
            return true;
        }
        return dateDeath.isAfter(artist.getDateBirth());
    }
}
