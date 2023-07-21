package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User get(Integer id);

    List<User> getAll();

    void setFriends(Integer userId, Set<Integer> friendSet);

    Set<Integer> getFriends(Integer userId);
}
