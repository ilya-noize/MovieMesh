package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Integer generateId;
    private final Map<Integer, User> users;
    private final Map<Integer, Set<Integer>> friends;

    public InMemoryUserStorage() {
        generateId = 1;
        this.users = new HashMap<>();
        this.friends = new HashMap<>();
    }

    @Override
    public User create(User user) {
        if (user.isLoginUnical(users)) { // todo in service
            user.setId(generateId++);
            user.nameEqualLoginIfNameIsNullOrBlank(); // todo in service

            users.put(user.getId(), user);
            friends.put(user.getId(), new HashSet<>());
            return user;
        }
        throw new UserAlreadyExistException("User with login:" + user.getLogin() + "exist");
    }

    @Override
    public User update(User user) {
        Integer userId = user.getId();
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с уин:" + userId + " не зарегистрирован.");
        }
        user.nameEqualLoginIfNameIsNullOrBlank(); // todo in service
        users.replace(userId, user);
        return user;
    }

    @Override
    public User get(Integer id) {
        return users.get(id);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean addFriend(Integer userId, Integer friendId) {
//        User user = users.get(userId);
//        User friend = users.get(friendId);
//        if (user.addFriend(friendId) && friend.addFriend(userId)) {
//            users.replace(userId, user);
//            users.replace(friendId, friend);
        Set<Integer> userFriends = friends.get(userId);
        Set<Integer> friendFriends = friends.get(friendId);
        if (userFriends.add(friendId) && friendFriends.add(userId)) {
            friends.replace(userId, userFriends);
            friends.replace(friendId, friendFriends);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteFriend(Integer userId, Integer friendId) {
//        User user = users.get(userId);
//        User friend = users.get(friendId);
//        if (user.deleteFriend(friendId) && friend.deleteFriend(userId)) {
//            users.replace(userId, user);
//            users.replace(friendId, friend);
        Set<Integer> userFriends = friends.get(userId);
        Set<Integer> friendFriends = friends.get(friendId);
        if (userFriends.remove(friendId) && friendFriends.remove(userId)) {
            friends.replace(userId, userFriends);
            friends.replace(friendId, friendFriends);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Set<Integer> getFriends(Integer userId) {
//        return friends.get(userId).stream()
//                .map(users::get)
//                .collect(Collectors.toSet());
        return friends.get(userId);
    }

}
