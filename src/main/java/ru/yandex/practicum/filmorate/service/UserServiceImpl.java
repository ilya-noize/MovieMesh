package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    UserStorage userStorage;

    /**
     * создание пользователя
     *
     * @param user пользователь
     * @return пользователь
     */
    public User create(User user) {
        log.info("* Создание пользователя");
        user = userStorage.create(validation(user));
        log.info("Пользователь с логином:{} создан", user.getLogin());
        return user;
    }

    /**
     * обновление пользователя
     *
     * @param user пользователь
     * @return пользователь
     */
    public User update(User user) {
        log.info("* Обновление данных пользователя");
        user = userStorage.update(validation(user));
        log.info("Пользователь с логином:{} обновлён", user.getLogin());
        return user;
    }

    /**
     * возвращает пользователя.
     *
     * @param supposedId уин пользователя
     * @return пользователь
     * @see #getUserFromData(String)
     */
    public User get(String supposedId) {
//        User user = getUserFromData(supposedId);
//        log.info("* Возвращаем пользователя {}", user.getLogin());
        return getUserFromData(supposedId);
    }

    /**
     * получение списка всех пользователей
     *
     * @return список
     */
    public List<User> getAll() {
        log.info("* Получаем список всех пользователей");
        return userStorage.getAll();
    }

    /**
     * добавление в друзья
     *
     * @param supposedId       уин пользователя
     * @param supposedIdFriend уин пользователя-друга
     */
    public void addFriend(String supposedId, String supposedIdFriend) {
        User user = get(supposedId);
        Integer userId = user.getId();
        Set<Integer> userFriends = userStorage.getFriends(userId);

        User userFriend = get(supposedIdFriend);
        Integer userFriendId = userFriend.getId();
        Set<Integer> userFriendFriends = userStorage.getFriends(userFriendId);

        log.info("* Добавление в друзья {}<->{}", user.getLogin(), userFriend.getLogin());

        if (userFriends.add(userFriendId) && userFriendFriends.add(userId)) {
            userStorage.setFriends(userId, userFriends);
            userStorage.setFriends(userFriendId, userFriendFriends);
        } else {
            throw new FailSetFriendException("Неудачное добавление в список друзей");
        }
    }

    /**
     * удаление из друзей.
     *
     * @param supposedId       уин пользователя
     * @param supposedIdFriend уин пользователя-друга
     */
    public void deleteFriend(String supposedId, String supposedIdFriend) {
        User user = get(supposedId);
        Integer userId = user.getId();
        Set<Integer> userFriends = userStorage.getFriends(userId);

        User userFriend = get(supposedIdFriend);
        Integer userFriendId = userFriend.getId();
        Set<Integer> userFriendFriends = userStorage.getFriends(userFriendId);

        log.info("* Пользователь {} удаляет друга {} из списка друзей", user.getLogin(), userFriend.getLogin());

        if (userFriends.remove(userFriendId) && userFriendFriends.remove(userId)) {
            userStorage.setFriends(userId, userFriends);
            userStorage.setFriends(userFriendId, userFriendFriends);
        } else {
            throw new FailSetFriendException("Неудачное удаление из списка друзей");
        }
    }

    /**
     * возвращает список пользователей, являющихся друзьями пользователя.
     *
     * @param supposedId уин пользователя
     * @return список
     */
    public Set<User> getFriends(String supposedId) {
        User user = get(supposedId);

        log.info("* Возвращаем список пользователей, являющихся друзьями пользователя {}", user.getLogin());
        Set<Integer> friendsIdUser = userStorage.getFriends(user.getId());

        return getFriendsSet(friendsIdUser);
    }

    /**
     * возвращает список общих друзей между пользователями
     *
     * @param supposedId      уин пользователя №1
     * @param supposedOtherId уин пользователя №2
     * @return список
     */
    public Set<User> getFriendsCommon(String supposedId, String supposedOtherId) {
        User user = get(supposedId);
        User userOther = get(supposedOtherId);

        log.info("* Возвращаем список общих друзей между пользователями {}<->{}", user.getLogin(), userOther.getLogin());
        Set<Integer> friendsIdUser = userStorage.getFriends(user.getId());
        Set<Integer> friendsIdUserOther = userStorage.getFriends(userOther.getId());
        friendsIdUser.retainAll(friendsIdUserOther);

//        log.info("Найдено совпадений {}", friendsIdUser.size());
        return getFriendsSet(friendsIdUser);
    }

    /**
     * возвращает список пользователей-друзей
     *
     * @param friendsIdSet список уин пользователей
     * @return список
     * @see #getFriends
     * @see #getFriendsCommon(String, String)
     * @see #getAll()
     */
    private Set<User> getFriendsSet(Set<Integer> friendsIdSet) {
        log.info("* Возвращаем список пользователей-друзей");
        Set<User> friendsSet = friendsIdSet.stream()
                .map(userStorage::get)
                .collect(Collectors.toSet());
        if (friendsSet.size() == 0) {
            throw new EmptyFriendsSetException("Список пользователей-друзей пуст");
        }
        return friendsSet;
    }

    /**
     * Преобразование строки в число
     *
     * @param supposedInt Строка
     * @return число
     * @see #getUserFromData(String)
     */
    private Integer IntegerFromString(String supposedInt) {
        try {
            return Integer.valueOf(supposedInt);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Возвращает пользователя, после всех проверок
     *
     * @param supposedId предполагаемый уин пользователя в строке
     * @return пользователь
     * @see #IntegerFromString(String)
     * @see #get(String)
     */
    private User getUserFromData(String supposedId) {
//        log.info("* Попытка получить данные пользователя");
        Integer userId = IntegerFromString(supposedId);
        if (userId == Integer.MIN_VALUE || userId <= 0) {
            String error = String.format("Неверный уин пользователя: %d", userId);
            log.error(error);
            throw new WrongIdException(error);
        }
        User user = userStorage.get(userId);
        if (user == null) {
            String error = String.format("Пользователь не найден: id:%d не зарегистрирован", userId);
            log.error(error);
            throw new NotFoundException(error);
        }
//        log.info("Успешно получены данные пользователя");
        return user;
    }

    private User validation(User user) {
        loginUnical(user);
        user.setName(checkName(user));
        return user;
    }

    private void loginUnical(User user) {
//        log.info("* Проверка уникальности логина пользователя");
        String login = user.getLogin();
        for (User userCheck : getAll()) {
            if (login.equals(userCheck.getLogin())) {
                String error = String.format("Пользователь с логином:%s уже существует.", login);
                log.error(error);
                throw new UserAlreadyExistException(error);
            }
        }
//        log.info("Логин пользователя соответствует требованиям");
    }

    private String checkName(User user) {
//        log.info("* Проверка имени пользователя");
        String name = user.getName();
        if (name == null || name.isBlank()) {
//            log.info("Имя пользователя теперь его логин");
            return user.getLogin();
        } else {
//            log.info("Имя пользователя соответствует требованиям");
            return name;
        }
    }
}
