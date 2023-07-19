package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User get(Integer id);

    User ifExist(Integer id);

    void friendlyUser(Integer userId, Integer friendlyUserId);

    void unFriendlyUser(Integer userId, Integer unFriendlyUserId);

    Set<User> getUserFriends(Integer userId);

    Set<User> getUserFriendsCommon(Integer userId, Integer otherUserId);

    List<User> getAll();
}
