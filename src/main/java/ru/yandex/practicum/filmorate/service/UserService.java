package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDAO userDAO;

    /**
     * создание пользователя
     *
     * @param user пользователь
     * @return пользователь
     */
    public User create(User user) {
        return userDAO.create(valid(user));
    }

    /**
     * обновление пользователя
     *
     * @param user пользователь
     * @return пользователь
     */
    public User update(User user) {
        return userDAO.update(valid(user));
    }

    public User get(Long id) {
        return userDAO.get(id);
    }

    public void createFriend(long id, long friendId) {
        log.debug("[i][S] User\t createFriend(long {}, long {})", id, friendId);
        if (id == friendId) {
            log.error("[!][S] User\t friendship yourself");
            throw new FriendsException();
        }
        isExist(id, friendId);
        userDAO.createFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        if (id.equals(friendId)) {
            throw new FriendsException();
        }
        isExist(id, friendId);
        userDAO.deleteFriend(id, friendId);
    }

    /**
     * возвращает список пользователей, являющихся друзьями пользователя.
     *
     * @param id уин пользователя
     * @return список
     */
    public List<User> getFriends(Long id) {
        isExist(id);

        log.debug("Returning the user's friends list {}", id);
        return userDAO.getFriends(id);
    }

    /**
     * возвращает список общих друзей между пользователями
     *
     * @param id      уин пользователя №1
     * @param otherId уин пользователя №2
     * @return список
     */
    public List<User> getCommonFriends(Long id, Long otherId) {
        isExist(id, otherId);
        if (id.equals(otherId)) {
            throw new FriendsException();
        }

        log.debug("Returning a list of friends between users {}<->{}", id, otherId);
        return userDAO.getCommonFriends(id, otherId);
    }

    public List<User> getAll() {
        return userDAO.getAll();
    }

    private void isExist(Long... ids) {
        for (Long id : ids) {
            get(id);
        }
    }

    /**
     * Валидация данных о пользователе
     *
     * @param user пользователь
     * @return пользователь
     */
    private User valid(User user) {
        return new User(
                user.getId(),
                user.getEmail(),
                loginUnical(user),
                checkName(user),
                user.getBirthday()
        );
    }

    /**
     * Проверка логина на уникальность
     *
     * @param user пользователь
     */
    private String loginUnical(User user) {
        log.debug("[?] unical login.");
        String login = user.getLogin();

        final Predicate<User> TWIN_LOGIN = user1 -> user1.getLogin().equals(login);
        Optional<User> result = getAll().stream().filter(TWIN_LOGIN).findFirst();

        if (result.isPresent()) {
            String error = String.format("User with login:%s exist.", login);
            log.error(error);
            throw new UserAlreadyExistException(error);
        }
        log.debug("Login correct.");
        return login;
    }

    /**
     * Проверка имени пользователя
     *
     * @param user пользователь
     * @return имя пользователя
     */
    private String checkName(User user) {
        log.debug("[?] Username.");
        String name = user.getName();
        if (name == null || name.isBlank()) {
            log.debug("Username is login.");
            return user.getLogin();
        } else {
            log.debug("Username correct.");
            return name;
        }
    }
}