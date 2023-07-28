package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;

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

    public abstract List<T> getAll();

    public abstract boolean isExist(Long id);
}
