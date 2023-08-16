package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class User {
    @EqualsAndHashCode.Exclude
    @Positive(message = "The user ID is a positive natural number.")
    private Long id;

    @Email(message = "The entered value is not an email address.")
    private String email;

    @Pattern(regexp = "^\\S*$", message = "The login cannot contain spaces.")
    @Size(min = 3, max = 20, message = "The login must be from 3 to 20 characters.")
    private String login;
    private String name;

    @Past(message = "The date of birth should only be in the past.")
    private LocalDate birthday;
    private Set<Long> friends;
    private Set<Long> likes;

    public User(String login, String name, String email, LocalDate birthday, Set<Long> friends) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.friends = friends;
    }
}