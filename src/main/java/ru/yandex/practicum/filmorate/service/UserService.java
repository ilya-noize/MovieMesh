package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage memoryUserStorage) {
        this.userStorage = memoryUserStorage;
    }

    /**
     * создание пользователя
     * @param user  пользователь
     * @return пользователь
     */
    public User create(User user) {
        return userStorage.create(user);
    }

    /**
     * обновление пользователя
     * @param user пользователь
     * @return пользователь
     */
    public User update(User user) {
        return userStorage.update(user);
    }

    /**
     * добавление в друзья
     * @param supposedId уин пользователя
     * @param supposedIdFriend уин пользователя-друга
     */
    public void addFriendUser(String supposedId, String supposedIdFriend) {
        User user = get(supposedId);
        Integer userId = user.getId();

        User userFriend = get(supposedIdFriend);
        Integer userIdFriend = userFriend.getId();

        userStorage.addFriend(userId, userIdFriend);

//        boolean friends01 = user.addFriend(userIdFriend);
//        boolean friends10 = userFriend.addFriend(userId);
//
//        if (friends01 && friends10) {
//            update(user);
//            update(userFriend);
//        }
    }

    /**
     * удаление из друзей.
     * @param supposedId уин пользователя
     * @param supposedIdFriend уин пользователя-друга
     */
    public void deleteFriendUser(String supposedId, String supposedIdFriend) {
        User user = get(supposedId);
        Integer userId = user.getId();

        User userFriend = get(supposedIdFriend);
        Integer userIdFriend = userFriend.getId();

        userStorage.deleteFriend(userId, userIdFriend);

//        boolean friends01 = user.deleteFriend(userIdFriend);
//        boolean friends10 = userFriend.deleteFriend(userId);
//
//        if (friends01 && friends10) {
//            update(user);
//            update(userFriend);
//        }
    }

    /**
     * возвращает список пользователей-друзей
     * @see #getUserFriends
     * @see #getUserFriendsCommon(String, String)
     * @see #getAll()
     *
     * @param friendsIdSet список уин пользователей
     * @return список
     */
    private Set<User> getFriendsUserSet(Set<Integer> friendsIdSet) {
        return friendsIdSet.stream()
                .map(getAll()::get)
                .collect(Collectors.toSet());
    }

    /**
     * возвращает список пользователей, являющихся друзьями пользователя.
     * @param supposedId уин пользователя
     * @return список
     */
    public Set<User> getUserFriends(String supposedId) {
//        Set<Integer> friends = get(supposedId).getFriends();
//        return getFriendsUserSet(friends);

        User user = get(supposedId);
        Set<Integer> friendsIdUser = userStorage.getFriends(user.getId());

        return getFriendsUserSet(friendsIdUser);
    }

    /**
     * возвращает список общих друзей между пользователями
     * @param supposedId  уин пользователя №1
     * @param supposedOtherId уин пользователя №2
     * @return список
     */
    public Set<User> getUserFriendsCommon(String supposedId, String supposedOtherId) {
//        Set<Integer> friendsUser = get(supposedId).getFriends();
//        Set<Integer> friendsOtherUser = get(supposedOtherId).getFriends();
//        friendsUser.retainAll(friendsOtherUser);
//
//        return getFriendsUserSet(friendsUser);

        User user = get(supposedId);
        User userOther = get(supposedOtherId);
        Set<Integer> friendsIdUser = userStorage.getFriends(user.getId());
        Set<Integer> friendsIdUserOther = userStorage.getFriends(userOther.getId());
        friendsIdUser.retainAll(friendsIdUserOther);

        return getFriendsUserSet(friendsIdUser);
    }

    /**
     * получение списка всех пользователей
     * @return список
     */
    public List<User> getAll() {
        return userStorage.getAll();
    }

    /**
     * возвращает пользователя.
     * @see #getUserFromData(String)
     *
     * @param supposedId уин пользователя
     * @return пользователь
     */
    public User get(String supposedId) {
        return getUserFromData(supposedId);
    }

    /**
     * Преобразование строки в число
     * @see #getUserFromData(String)
     *
     * @param supposedId Строка
     * @return число
     */
    private Integer idFromString(String supposedId) {
        try {
            return Integer.valueOf(supposedId);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Возвращает пользователя, после всех проверок
     * @see #idFromString(String)
     *
     * @param supposedId предполагаемый уин пользователя в строке
     * @return пользователь
     * @see #get(String)
     */
    private User getUserFromData(String supposedId) {
        Integer userId = idFromString(supposedId);
        if (userId == Integer.MIN_VALUE) {
            throw new WrongIdException("Распознать уин пользователя не удалось: id " + supposedId);
        }
        User user = userStorage.get(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь не найден: id:" + userId + " не зарегистрирован");
        }
        return user;
    }
}
