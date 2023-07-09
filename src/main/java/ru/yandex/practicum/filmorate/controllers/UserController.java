package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ExistException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController extends Controller {
    private final Map<Integer, User> users;

    public UserController() {
        users = new HashMap<>();
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) throws RuntimeException {
        log.info("Получен запрос POST к эндпоинту: /users");

        if (user.isLoginUnical(users)) {
            user.setId(generateId++);
            user.nameEqualLoginIfNameIsNullOrBlank();
            users.put(user.getId(), user);
            log.info("Добавлен пользователь(id={},login={})", user.getId(), user.getLogin());
            return user;
        }
        throw new ExistException("Пользователь с таким логином уже зарегистрирован");
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        log.info("Получен запрос PUT к эндпоинту: /users");
        Integer userId = user.getId();
        if (users.containsKey(userId)) {
            user.nameEqualLoginIfNameIsNullOrBlank();
            users.replace(userId, user);
            log.info("Изменён пользователь(id={},login={})", userId, user.getLogin());
            return user;
        }
        throw new NotFoundException("Нет такого пользователя.");
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Получен запрос GET к эндпоинту: /users");
        return new ArrayList<>(users.values());
    }
}