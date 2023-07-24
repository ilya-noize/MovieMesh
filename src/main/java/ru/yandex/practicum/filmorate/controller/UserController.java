package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User create(@RequestBody @Valid User user) {
        log.info("Новый пользователь:{}", user);
        return userService.create(user);
    }

    @PutMapping
    public User update(@RequestBody @Valid User user) {
        log.info("Изменение пользователя:{}", user);
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Добавление пользователю-id:{} друга-id:{}", id, friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        log.info("Удаление у пользователя-id:{} друга-id:{}", id, friendId);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        log.info("Получение пользователя-id:{}", id);
        return userService.get(id);
    }

    @GetMapping("/{id}/friends")
    public Set<User> getUserFriends(@PathVariable Long id) {
        log.info("Получение списка друзей у пользователя-id:{}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getUserCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получение списка общих друзей у пользователей id1:{} и id2{}", id, otherId);
        return userService.getFriendsCommon(id, otherId);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.getAll();
    }
}