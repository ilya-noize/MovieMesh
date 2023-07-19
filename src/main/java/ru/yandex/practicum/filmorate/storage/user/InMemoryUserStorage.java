package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ExistException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Integer generateId;
    private final Map<Integer, User> users;

    public InMemoryUserStorage() {
        generateId = 1;
        this.users = new HashMap<>();
    }

    @Override
    public User create(User user) {
        if (user.isLoginUnical(users)) {
            user.setId(generateId++);
            user.nameEqualLoginIfNameIsNullOrBlank();

            users.put(user.getId(), user);
            return user;
        }
        throw new ExistException("User with login:" + user.getLogin() + "exist");
    }

    @Override
    public User update(User user) {
        user = ifExist(user.getId());
        user.nameEqualLoginIfNameIsNullOrBlank();
        users.replace(user.getId(), user);
        return user;
    }

    @Override
    public User get(Integer id) {
        return users.get(id);
    }

    @Override
    public User ifExist(Integer id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Нет такого пользователя.");
        } else {
            return users.get(id);
        }
    }

    @Override
    public void friendlyUser(Integer userId, Integer userIdFriend) {
        User user = ifExist(userId);
        User userFriend = ifExist(userIdFriend);

        if (user.getFriends().add(userIdFriend)
                && userFriend.getFriends().add(userId)) {
            users.replace(userId, user);
            users.replace(userIdFriend, userFriend);
        }
    }

    @Override
    public void unFriendlyUser(Integer userId, Integer userIdFriend) {
        User user = ifExist(userId);
        User userFriend = ifExist(userIdFriend);

        if (user.getFriends().remove(userIdFriend)
                && userFriend.getFriends().remove(userId)) {
            users.replace(userId, user);
            users.replace(userIdFriend, userFriend);
        }
    }

    @Override
    public Set<User> getUserFriends(Integer userId) {
        Set<Integer> friends = ifExist(userId).getFriends();
        return friends.stream()
                .map(users::get)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getUserFriendsCommon(Integer userId, Integer otherUserId) {
        Set<Integer> friendsUser = ifExist(userId).getFriends();
        Set<Integer> friendsOtherUser = ifExist(otherUserId).getFriends();
        friendsUser.retainAll(friendsOtherUser);
        return friendsUser.stream()
                .map(users::get)
                .collect(Collectors.toSet());
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
}
