package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

public interface UserService extends FriendsService, CommittingService<User>, ShowableService<User> {
    void isExist(Long... ids);
}
