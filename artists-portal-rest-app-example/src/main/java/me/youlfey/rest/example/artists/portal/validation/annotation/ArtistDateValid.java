package me.youlfey.rest.example.artists.portal.validation.annotation;

import me.youlfey.rest.example.artists.portal.validation.DateForArtistValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateForArtistValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArtistDateValid {
    String message() default "error date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
