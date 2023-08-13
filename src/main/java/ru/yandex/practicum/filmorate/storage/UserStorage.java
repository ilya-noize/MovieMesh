package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserStorage extends MasterStorage<User> {
    private final Map<Long, User> storage;

    @Override
    public User create(User user) {
        user.setId(increment());
        Long userId = user.getId();
        storage.put(userId, user);
        log.info("create User({})", userId);
        return user;
    }

    @Override
    public User update(User user) {
        Long userId = user.getId();
        if (isExist(userId)) {
            storage.replace(userId, user);
        }
        log.info("update User({})", userId);
        return user;
    }

    @Override
    public User get(Long id) {
        User user = null;
        if (isExist(id)) {
            user = storage.get(id);
            Long userId = user.getId();
            log.info("get User({})", userId);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean isExist(Long id) {
        if (!storage.containsKey(id)) {
            String error = String.format("User not found - id:%d not exist", id);
            throw new NotFoundException(error);
        }
        return true;
    }
}
