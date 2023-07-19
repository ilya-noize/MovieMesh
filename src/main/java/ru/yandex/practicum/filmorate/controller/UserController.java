package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController extends Controller {

    UserService userService;


    public UserController(@Autowired(required = false) UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Получен запрос POST к endpoint: /users");
        user = userService.create(user);
        log.info("Добавлен пользователь(id={},login={})", user.getId(), user.getLogin());
        return user;
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        log.info("Получен запрос PUT к endpoint: /users");
        userService.update(user);
        log.info("Изменён пользователь(id={},login={})", user.getId(), user.getLogin());
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable String userId, String friendId) {
        log.info("Получен запрос PUT к endpoint: /users/{}/friends/{}", userId, friendId);
        userService.addFriendUser(userId, friendId);
        log.info("Пользователи с id:{} и id:{} теперь друзья", userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable String userId, String friendId) {
        log.info("Получен запрос DELETE к endpoint: /users/{}/friends/{}", userId, friendId);
        userService.deleteFriendUser(userId, friendId);
        log.info("Пользователь с id:{} и с id:{} больше не друзья", userId, friendId);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable String id){
        log.info("Получен запрос GET к endpoint: /users/{}", id);
        return userService.get(id);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getUserFriends(@PathVariable String id){
        log.info("Получен запрос GET к endpoint: /users/{}/friends", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getUserCommonFriends(@PathVariable String id, String otherId){
        log.info("Получен запрос GET к endpoint: /users/{}/friends/common/{}", id, otherId);
        return userService.getUserFriendsCommon(id, otherId);
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Получен запрос GET к endpoint: /users");
        return userService.getAll();
    }

    @ExceptionHandler
    public Map<String, String> handleNegativeCount(final IllegalArgumentException e){
        return Map.of("error", "Передан отрицательный параметр count.");
    }

    @ExceptionHandler
    public Map<String, String> handleNullableCount(final NullPointerException e) {
        return Map.of("error", "Параметр count не указан.");
    }

    @ExceptionHandler
    public Map<String, String> handleError(final RuntimeException e) {
        return Map.of("error", e.getMessage());
    }
}