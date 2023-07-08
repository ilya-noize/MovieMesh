package ru.yandex.practicum.filmorate.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginWithValidator implements ConstraintValidator<LoginWith, String> {
    private char[] validChars;

    @Override
    public void initialize(LoginWith constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        validChars = constraintAnnotation.value().toCharArray();
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        for (char symbol: validChars) {
            if (login.contains("" + symbol)) {
                return true;
            }
        }
        return false;
    }
}