package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface UserService extends Service<User> {
    void addFriend(Long userId, Long friendId);

    Set<User> getFriends(Long id);

    Set<User> getFriendsCommon(Long userId, Long otherUserId);

    void deleteFriend(Long userId, Long friendId);
}
