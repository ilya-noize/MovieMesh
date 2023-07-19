package ru.yandex.practicum.filmorate.constraint;

import javax.validation.Constraint;
//import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = {CorrectReleaseDateValidator.class})
public @interface CorrectReleaseDate {
    String value();

    String message() default "Дата релиза не раньше определённой даты и не позже сегодняшней";

//    Class<?>[] groups() default {};

//    Class<? extends Payload>[] payload() default {};
}