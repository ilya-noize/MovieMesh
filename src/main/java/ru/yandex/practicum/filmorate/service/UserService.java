package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User create(User user);

    User update(User user);

    User get(String id);

    List<User> getAll();

    void addFriend(String userId, String friendId);

    Set<User> getFriends(String id);

    Set<User> getFriendsCommon(String userId, String otherUserId);

    void deleteFriend(String userId, String friendId);
}
