package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@RequiredArgsConstructor
public abstract class MasterStorage<T> {
    private Long generateId = 1L;

    protected Long increment() {
        return generateId++;
    }

    public abstract T create(T t);

    public abstract T update(T t);

    public abstract T get(Long id);

    public abstract void delete(Long... id);

    public abstract List<T> getAll();

    public boolean isExist(Long id) {
        if (this.get(id) == null) {
            String error = String.format("User not found - id:%d not exist", id);
            throw new NotFoundException(error);
        }
        return true;
    }
}
