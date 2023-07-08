package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.FilmorateApplication.log;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users;
    private int generateId = 1;

    public UserController() {
        users = new HashMap<>();
    }

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        log.info("Получен запрос POST к эндпоинту: /users");

        if (user.isLoginUnical(users)) {
            user.setId(generateId++);

            if (user.getName() == null) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Добавлен пользователь(id={},login={})", user.getId(), user.getLogin());
            return user;
        }
        throw new RuntimeException("Пользователь с таким логином уже зарегистрирован");
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        log.info("Получен запрос PUT к эндпоинту: /users");
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            log.info("Изменён пользователь(id={},login={})", user.getId(), user.getLogin());
            return user;
        }
        throw new RuntimeException("Нет такого пользователя.");
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Получен запрос GET к эндпоинту: /users");
        return new ArrayList<>(users.values());
    }
}