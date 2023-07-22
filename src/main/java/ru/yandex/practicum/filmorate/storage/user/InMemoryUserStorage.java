package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Integer generateId;
    private final Map<Integer, User> users;

    @Autowired
    public InMemoryUserStorage() {
        this.generateId = 1;
        this.users = new HashMap<>();
    }

    @Override
    public User create(User user) {
        user.setId(generateId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.replace(user.getId(), user);
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
    public void setFriends(Integer userId, Set<Integer> userFriends) {
        users.get(userId).setFriends(userFriends);
    }

    @Override
    public Set<Integer> getFriends(Integer userId) {
        return users.get(userId).getFriends();
    }
}