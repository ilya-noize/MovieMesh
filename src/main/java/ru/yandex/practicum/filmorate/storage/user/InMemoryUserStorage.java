package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users;
    private Long generateId = 1L;

    @Override
    public User create(User user) {
        user.setId(generateId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (isExist(user.getId())) {
            users.replace(user.getId(), user);
        }
        return user;
    }

    @Override
    public User get(Long id) {
        User user = null;
        if (isExist(id)) {
            user = users.get(id);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean isExist(Long id) {
        if (!users.containsKey(id)) {
            String error = String.format("Пользователь не найден: id:%d не зарегистрирован", id);
            log.error(error);
            throw new NotFoundException(error);
        }
        return true;
    }

    @Override
    public void setFriends(Long userId, Set<Long> userFriends) {
        users.get(userId).setFriends(userFriends);
    }

    @Override
    public Set<Long> getFriends(Long userId) {
        return users.get(userId).getFriends();
    }
}