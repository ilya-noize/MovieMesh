package ru.yandex.practicum.filmorate.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class CorrectReleaseDateValidator implements ConstraintValidator<CorrectReleaseDate, LocalDate> {
    private LocalDate releaseDateStart;

    @Override
    public void initialize(CorrectReleaseDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        releaseDateStart = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        return date.isAfter(releaseDateStart) && date.isBefore(LocalDate.now());
    }
}