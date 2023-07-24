package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FailSetFriendException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

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

    private User validation(User user) {
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

    /**
     * возвращает пользователя.
     *
     * @param id уин пользователя
     * @return пользователь
     */
    public User get(Long id) {
        User user = userStorage.get(id);
        log.info("get User({})", id);
        return user;
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
     * @param id       уин пользователя
     * @param idFriend уин пользователя-друга
     */
    public void addFriend(Long id, Long idFriend) {
        if (userStorage.get(id) != null && userStorage.get(idFriend) != null) {
            addFriendByOne(id, idFriend);
            addFriendByOne(idFriend, id);
        }
    }

    private void addFriendByOne(Long id, Long idFriend) {
        Set<Long> userFriends = userStorage.getFriends(id);
        if (userFriends.add(idFriend)) {
            userStorage.setFriends(id, userFriends);
        } else {
            String error = "Неудачное добавление в список друзей";
            log.error(error);
            throw new FailSetFriendException(error);
        }
    }

    /**
     * удаление из друзей.
     *
     * @param supposedId       уин пользователя
     * @param supposedIdFriend уин пользователя-друга
     */
    public void deleteFriend(Long supposedId, Long supposedIdFriend) {
        User user = get(supposedId);
        Long userId = user.getId();
        Set<Long> userFriends = userStorage.getFriends(userId);

        User userFriend = get(supposedIdFriend);
        Long userFriendId = userFriend.getId();
        Set<Long> userFriendFriends = userStorage.getFriends(userFriendId);

        log.info("* Удаление из друзей {}< - >{}", user.getLogin(), userFriend.getLogin());

        if (userFriends.remove(userFriendId) && userFriendFriends.remove(userId)) {
            userStorage.setFriends(userId, userFriends);
            userStorage.setFriends(userFriendId, userFriendFriends);
        } else {
            String error = "Неудачное удаление из списка друзей";
            log.error(error);
            throw new FailSetFriendException(error);
        }
        log.info("* Удаление завершено {}< o >{}", user.getId(), userFriend.getId());
    }

    /**
     * возвращает список пользователей, являющихся друзьями пользователя.
     *
     * @param supposedId уин пользователя
     * @return список
     */
    public Set<User> getFriends(Long supposedId) {
        User user = get(supposedId);

        log.info("* Возвращаем список пользователей, являющихся друзьями пользователя {}", user.getLogin());
        Set<Long> friendsIdUser = userStorage.getFriends(user.getId());

        return getFriendsSet(friendsIdUser);
    }

    /**
     * возвращает список общих друзей между пользователями
     *
     * @param id      уин пользователя №1
     * @param otherId уин пользователя №2
     * @return список
     */
    public Set<User> getFriendsCommon(Long id, Long otherId) {
        User user = get(id);
        User userOther = get(otherId);

        log.info("* Возвращаем список общих друзей между пользователями {}<->{}", user.getLogin(), userOther.getLogin());
        Set<Long> friendsIdUser = userStorage.getFriends(user.getId());
        Set<Long> friendsIdUserOther = userStorage.getFriends(userOther.getId());

        return getFriendsSet(findFriendsCommon(friendsIdUser, friendsIdUserOther));
    }

    /**
     * Поиск общих чисел в HashSet-ах. А именно,
     * аналог HashSet1<T>.retainAll(HashSet2<T>), но (!)
     * без затирания HashSet1
     *
     * @param friendsIdUser      HashSet1
     * @param friendsIdUserOther HashSet2
     * @return пересечение HashSet1 и HashSet2 - другой HashSet3
     */
    private Set<Long> findFriendsCommon(Set<Long> friendsIdUser, Set<Long> friendsIdUserOther) {
        return friendsIdUser.stream()
                .filter(friendsIdUserOther::contains)
                .collect(Collectors.toSet());
    }

    /**
     * возвращает список пользователей-друзей
     *
     * @param friendsIdSet список уин пользователей
     * @return список
     * @see #getFriends
     * @see #getAll()
     */
    private Set<User> getFriendsSet(Set<Long> friendsIdSet) {
        log.info("* Возвращаем список пользователей-друзей");
        Set<User> friendsSet = friendsIdSet.stream()
                .map(userStorage::get)
                .collect(Collectors.toSet());
        if (friendsSet.size() == 0) {
            return new HashSet<>();
        }
        return friendsSet;
    }
}