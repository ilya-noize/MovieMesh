package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public abstract class MainStorage<T> {
    private Long generateId = 1L;

    public abstract T create(T user);

    public abstract T update(T user);

    public abstract T get(Long id);

    public abstract List<T> getAll();

    public abstract boolean isExist(Long id);
}
