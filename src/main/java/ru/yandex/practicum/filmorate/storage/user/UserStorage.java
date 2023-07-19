package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User get(Integer id);

    List<User> getAll();

    boolean addFriend(Integer userId, Integer friendId);

    boolean deleteFriend(Integer userId, Integer friendId);
}
