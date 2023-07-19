package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;

@Service
public class UserService{
    UserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage memoryUserStorage){
        this.userStorage = memoryUserStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public void friendlyUser(Integer userId, Integer friendId) {
        userStorage.friendlyUser(userId, friendId);
    }

    public void unFriendlyUser(Integer userId, Integer unFriendId) {
        userStorage.unFriendlyUser(userId, unFriendId);
    }

    public Set<User> getUserFriends(Integer id) {
        return userStorage.getUserFriends(id);
    }

    public Set<User> getUserFriendsCommon(Integer userId, Integer otherUserId) {
        return userStorage.getUserFriendsCommon(userId, otherUserId);
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }
}
