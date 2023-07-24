package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    @EqualsAndHashCode.Exclude
    @Positive
    private Long id;

    @Email(message = "Введенное значение не является адресом электронной почты.")
    private String email;

    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы.")
    @Size(min = 3, max = 20, message = "Логин должен быть от 3 до 20 символов.")
    private String login;
    private String name;

    @Past(message = "Дата рождения должна быть только в прошлом.")
    private LocalDate birthday;
    private Set<Long> friends;

    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.friends = new HashSet<>();
    }
}