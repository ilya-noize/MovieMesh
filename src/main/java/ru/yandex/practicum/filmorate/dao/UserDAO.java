package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserDAO extends Showable<User>, Committing<User> {
    void createFriend(Long userRequest, Long userFriend);

    void deleteFriend(Long userRequest, Long userFriend);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);
}
