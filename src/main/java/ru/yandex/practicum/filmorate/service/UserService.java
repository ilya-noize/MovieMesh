package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;


@Slf4j
@Service
public abstract class UserService extends MainService<User> {

    @Autowired
    protected UserService(UserStorage storage) {
        super(storage);
    }

    /**
     * создание пользователя
     *
     * @param user пользователь
     * @return пользователь
     */
    @Override
    public User create(User user) {
        log.info("* Создание пользователя");
        user = storage.create(valid(user));
        log.info("Пользователь с логином:{} создан", user.getLogin());
        return user;
    }

    /**
     * обновление пользователя
     *
     * @param user пользователь
     * @return пользователь
     */
    @Override
    public User update(User user) {
        log.info("* Обновление данных пользователя");
        user = storage.update(valid(user));
        log.info("Пользователь с логином:{} обновлён", user.getLogin());
        return user;
    }

    /**
     * возвращает пользователя.
     *
     * @param id уин пользователя
     * @return пользователь
     */
    @Override
    public User get(Long id) {
        User user = storage.get(id);
        log.info("get User({})", id);
        return user;
    }

    /**
     * получение списка всех пользователей
     *
     * @return список
     */
    @Override
    public List<User> getAll() {
        log.info("* Получаем список всех пользователей");
        return storage.getAll();
    }

    @Override
    public User valid(User user) {
        loginUnical(user);
        user.setName(checkName(user));
        return user;
    }

    private void loginUnical(User user) {
        log.info("* Проверка уникальности логина пользователя");
        String login = user.getLogin();

        final Predicate<User> TWIN_LOGIN = user1 -> user1.getLogin().equals(login);
        Optional<User> result = getAll().stream().filter(TWIN_LOGIN).findFirst();

        if (result.isPresent()) {
            String error = String.format("Пользователь с логином:%s уже существует.", login);
            log.error(error);
            throw new UserAlreadyExistException(error);
        }
        log.info("Логин пользователя соответствует требованиям");
    }

    private String checkName(User user) {
        log.info("* Проверка имени пользователя");
        String name = user.getName();
        if (name == null || name.isBlank()) {
            log.info("Имя пользователя теперь его логин");
            return user.getLogin();
        } else {
            log.info("Имя пользователя соответствует требованиям");
            return name;
        }
    }
    public abstract void addFriend(Long id, Long idFriend);

    public abstract void deleteFriend(Long supposedId, Long supposedIdFriend);

    public abstract Set<User> getFriends(Long supposedId);

    public abstract Set<User> getFriendsCommon(Long id, Long otherId);
}