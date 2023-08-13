package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FailSetFriendException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService extends MasterService<User> {
    @Autowired
    protected UserService(MasterStorage<User> userStorage) {
        super(userStorage);
    }

    /**
     * создание пользователя
     *
     * @param user пользователь
     * @return пользователь
     */
    @Override
    public User create(User user) {
        return super.create(valid(user));
    }

    /**
     * обновление пользователя
     *
     * @param user пользователь
     * @return пользователь
     */
    @Override
    public User update(User user) {
        return super.update(valid(user));
    }

    /**
     * добавление в друзья
     *
     * @param id       уин пользователя
     * @param idFriend уин пользователя-друга
     */
    public void addFriend(Long id, Long idFriend) {
        if (this.get(id) != null && this.get(idFriend) != null) {
            addFriendByOne(id, idFriend);
            addFriendByOne(idFriend, id);
        }
    }

    /**
     * удаление из друзей.
     *
     * @param supposedId       уин пользователя
     * @param supposedIdFriend уин пользователя-друга
     */
    public void deleteFriend(Long supposedId, Long supposedIdFriend) {
        User user = this.get(supposedId);
        Long userId = user.getId();
        Set<Long> userFriends = user.getFriends();

        User userFriend = this.get(supposedIdFriend);
        Long userFriendId = userFriend.getId();
        Set<Long> userFriendFriends = userFriend.getFriends();

        log.info("Removing from friends {}< - >{}", userId, userFriendId);

        if (userFriends.remove(userFriendId) && userFriendFriends.remove(userId)) {
            user.setFriends(userFriends);
            userFriend.setFriends(userFriendFriends);
        } else {
            String error = "Unsuccessful deletion from the friends list";
            log.error(error);
            throw new FailSetFriendException(error);
        }
        log.info("Deletion completed {}< o >{}", user.getId(), userFriend.getId());
    }

    /**
     * возвращает список пользователей, являющихся друзьями пользователя.
     *
     * @param supposedId уин пользователя
     * @return список
     */
    public Set<User> getFriends(Long supposedId) {
        User user = get(supposedId);

        log.info("Returning the user's friends list {}", user.getLogin());
        Set<Long> friendsIdUser = user.getFriends();

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

        log.info("Returning a list of friends between users {}<->{}", user.getLogin(), userOther.getLogin());
        Set<Long> friendsIdUser = user.getFriends();
        Set<Long> friendsIdUserOther = userOther.getFriends();
        Set<Long> friendsCommon = findFriendsCommon(friendsIdUser, friendsIdUserOther);

        return getFriendsSet(friendsCommon);
    }

    private void addFriendByOne(Long id, Long idFriend) {
        Set<Long> userFriends = this.get(id).getFriends();
        if (userFriends.add(idFriend)) {
            this.get(id).setFriends(userFriends);
        } else {
            String error = "Unsuccessful addition to the friends list";
            log.error(error);
            throw new FailSetFriendException(error);
        }
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
     * @param friendsIdSet список уин пользователей
     * @return список
     * @see #getFriends
     * @see #getAll()
     */
    private Set<User> getFriendsSet(Set<Long> friendsIdSet) {
        log.info("Get friends.");
        Set<User> friendsSet = friendsIdSet.stream()
                .map(this::get)
                .collect(Collectors.toSet());
        if (friendsSet.size() == 0) {
            return new HashSet<>();
        }
        return friendsSet;
    }

    /**
     * Валидация данных о пользователе
     *
     * @param user пользователь
     * @return пользователь
     */
    private User valid(User user) {
        loginUnical(user);
        user.setName(checkName(user));
        return user;
    }

    /**
     * Проверка логина на уникальность
     *
     * @param user пользователь
     */
    private void loginUnical(User user) {
        log.info("Check unical login.");
        String login = user.getLogin();

        final Predicate<User> TWIN_LOGIN = user1 -> user1.getLogin().equals(login);
        Optional<User> result = getAll().stream().filter(TWIN_LOGIN).findFirst();

        if (result.isPresent()) {
            String error = String.format("User with login:%s exist.", login);
            log.error(error);
            throw new UserAlreadyExistException(error);
        }
        log.info("Login correct.");
    }

    /**
     * Проверка имени пользователя
     *
     * @param user пользователь
     * @return имя пользователя
     */
    private String checkName(User user) {
        log.info("Check username.");
        String name = user.getName();
        if (name == null || name.isBlank()) {
            log.info("Username is login.");
            return user.getLogin();
        } else {
            log.info("Username correct.");
            return name;
        }
    }
}