package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class UserStorage extends MainStorage<User> {
    protected UserStorage(Map<Long, User> storage) {
        super(storage);
    }

    @Override
    public User create(User user) {
        user.setId(increment());
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (isExist(user.getId())) {
            storage.replace(user.getId(), user);
        }
        return user;
    }

    @Override
    public User get(Long id) {
        User user = null;
        if (isExist(id)) {
            user = storage.get(id);
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
            String error = String.format("Пользователь не найден: id:%d не зарегистрирован", id);
            throw new NotFoundException(error);
        }
        return true;
    }
}
