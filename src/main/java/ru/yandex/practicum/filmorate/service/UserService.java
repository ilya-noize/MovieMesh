package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {
    User create(User user);

    User update(User user);

    User get(Long id);

    List<User> getAll();

    void addFriend(Long userId, Long friendId);

    Set<User> getFriends(Long id);

    Set<User> getFriendsCommon(Long userId, Long otherUserId);

    void deleteFriend(Long userId, Long friendId);
}
