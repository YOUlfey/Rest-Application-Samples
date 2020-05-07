package me.youlfey.rest.example.artists.portal.validation.annotation;

import me.youlfey.rest.example.artists.portal.validation.StyleIdForArtistValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StyleIdForArtistValidation.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArtistStyleIdValid {
    String message() default "error style id";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
