package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * POST /users — создание пользователя;
 * PUT /users — обновление пользователя;
 * PUT /users/{id}/friends/{friendId} — добавление в друзья.
 * DELETE /users/{id}/friends/{friendId} — удаление из друзей.
 * GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
 * GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
 * GET /users — получение списка всех пользователей.
 */
@RestController
@RequestMapping("/users")
public class UserController extends Controller {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * создание пользователя
     * @param user  пользователь
     * @return пользователь
     */
    @PostMapping("/users")
    public User create(@RequestBody @Valid User user) {
        log.info("Получен запрос POST к endpoint: /users");
        user = userService.create(user);
        log.info("Добавлен пользователь(id={},login={})", user.getId(), user.getLogin());
        return user;
    }

    /**
     * обновление пользователя
     * @param user пользователь
     * @return пользователь
     */
    @PutMapping("/users")
    public User update(@RequestBody @Valid User user) {
        log.info("Получен запрос PUT к endpoint: /users");
        userService.update(user);
        log.info("Изменён пользователь(id={},login={})", user.getId(), user.getLogin());
        return user;
    }

    /**
     * добавление в друзья
     * @param userId уин пользователя
     * @param friendId  уин пользователя-друга
     */
    @PutMapping("/users/{id}/friends/{friendId}")
    public void friendlyUsers(@PathVariable Integer userId, Integer friendId) {
        log.info("Получен запрос PUT к endpoint: /users/{}/friends/{}", userId, friendId);
        userService.friendlyUser(userId, friendId);
        log.info("Пользователи с id:{} и id:{} теперь друзья", userId, friendId);
    }

    /**
     * удаление из друзей.
     * @param userId уин пользователя
     * @param friendId уин пользователя-друга
     */
    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void unFriendlyUsers(@PathVariable Integer userId, Integer friendId) {
        log.info("Получен запрос DELETE к endpoint: /users/{}/friends/{}", userId, friendId);
        userService.unFriendlyUser(userId, friendId);
        log.info("Пользователь с id:{} и с id:{} больше не друзья", userId, friendId);
    }

    /**
     * возвращаем список пользователей, являющихся его друзьями.
     * @param id уин пользователя
     * @return список пользователей
     */
    @GetMapping("/users/{id}/friends")
    public Set<User> getUserFriends(@PathVariable Integer id){
        log.info("Получен запрос GET к endpoint: /users/{}/friends", id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Set<User> getUserCommonFriends(@PathVariable Integer id, Integer otherId){
        log.info("Получен запрос GET к endpoint: /users/{}/friends/common/{}", id, otherId);
        return userService.getUserFriendsCommon(id, otherId);
    }

    /**
     * получение списка всех пользователей
     * @return список
     */
    @GetMapping
    public List<User> getAll() {
        log.info("Получен запрос GET к endpoint: /users");
        return userService.getAll();
    }
}