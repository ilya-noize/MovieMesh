package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Map;

@Data
@ToString
public class User {

    @EqualsAndHashCode.Exclude
    private Integer id;

    @Email(message = "Введенное значение не является адресом электронной почты.")
    private String email;

    @Pattern(regexp = "^\\S*$", message = "Логин не может содержать пробелы.")
    @Size(min = 3, max = 20, message = "Логин должен быть от 3 до 20 символов.")
    private String login;

    private String name;

    @Past(message = "Дата рождения должна быть только в прошлом.")
    private LocalDate birthday;

    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }

    public boolean isLoginUnical(Map<Integer, User> users) {
        for (User user: users.values()) {
            if (this.getLogin().equals(user.getLogin())) {
                return false;
            }
        }
        return true;
    }

    public void nameEqualLoginIfNameIsNullOrBlank() {
        if(name == null || name.isBlank()){
            name = login;
        }
    }
}