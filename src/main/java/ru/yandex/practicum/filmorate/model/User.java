package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

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
//    private final Set<Integer> friends;

    public User(String login, String name, String email, LocalDate birthday) {
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
//        friends = new HashSet<>();
    }
//
//    public boolean addFriend(Integer id) {
//        return friends.add(id);
//    }
//
//    public boolean deleteFriend(Integer id) {
//        return friends.remove(id);
//    }
}