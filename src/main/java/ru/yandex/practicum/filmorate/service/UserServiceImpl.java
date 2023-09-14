package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.exception.FriendsException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDAO userDAO;

    @Override
    public User create(User user) {
        return userDAO.create(valid(user));
    }

    @Override
    public User update(User user) {
        isExist(user.getId());
        return userDAO.update(valid(user));
    }

    @Override
    public User get(Long id) {
        return userDAO.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDAO.getAll();
    }

    @Override
    public void createFriend(long id, long friendId) {
        log.debug("[i][S] User\t createFriend(long {}, long {})", id, friendId);
        if (id == friendId) {
            log.error("[!][S] User\t friendship yourself");
            throw new FriendsException();
        }
        isExist(id, friendId);
        userDAO.createFriend(id, friendId);
    }

    @Override
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
    @Override
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
    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        isExist(id, otherId);
        if (id.equals(otherId)) {
            throw new FriendsException();
        }

        log.debug("Returning a list of friends between users {}<->{}", id, otherId);
        return userDAO.getCommonFriends(id, otherId);
    }

    public void isExist(Long... ids) {
        for (Long id : ids) {
            if (userDAO.isExist(id).equals(0L)) {
                String error = String.format("User not found - id:%d not exist", id);
                log.error(error);
                throw new NotFoundException(error);
            }
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
                loginUnique(user),
                checkName(user),
                user.getBirthday()
        );
    }

    /**
     * Проверка логина на уникальность
     *
     * @param user пользователь
     */
    private String loginUnique(User user) {
        String login = user.getLogin();
        log.debug("[?] Login:{} unique?", login);
        if (userDAO.isLoginUnique(login).equals(login)) {
            String error = String.format("User with login:%s exist.", login);
            log.error(error);
            throw new UserAlreadyExistException(error);
        }
        log.debug("[i] Login:{} correct.", login);
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