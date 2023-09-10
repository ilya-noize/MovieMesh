package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public final class User {
    @EqualsAndHashCode.Exclude
    @Positive(message = "The user ID is a positive natural number.")
    private final Long id;
    @Email(message = "The entered value is not an email address.")
    private final String email;
    @Pattern(regexp = "^\\S*$", message = "The login cannot contain spaces.")
    @Size(min = 3, max = 20, message = "The login must be from 3 to 20 characters.")
    private final String login;
    private final String name;
    @Past(message = "The date of birth should only be in the past.")
    private final LocalDate birthday;
}