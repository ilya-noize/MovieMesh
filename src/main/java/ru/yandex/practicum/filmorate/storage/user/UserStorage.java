package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User create(User user);

    User update(User user);

    User get(Long id);

    List<User> getAll();

    boolean isExist(Long id);

    void setFriends(Long userId, Set<Long> friendSet);

    Set<Long> getFriends(Long userId);
}
