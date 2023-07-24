package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.storage.MainStorage;

import java.util.List;

public abstract class MainService<T> {
    protected MainStorage<T> storage;

    protected MainService(MainStorage<T> storage) {
        this.storage = storage;
    }

    public abstract T create(T t);

    public abstract T update(T t);

    public abstract T get(Long id);

    public abstract List<T> getAll();

    public abstract T valid(T t);
}
