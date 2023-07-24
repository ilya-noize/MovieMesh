package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

public abstract class MainStorage<T> {
    private Long generateId = 1L;
    protected final Map<Long, T> storage;

    @Autowired
    protected MainStorage(Map<Long, T> storage) {
        this.storage = storage;
    }

    protected Long increment(){
        return generateId++;
    }

    public abstract T create(T t);

    public abstract T update(T t);

    public abstract T get(Long id);

    public abstract List<T> getAll();

    public abstract boolean isExist(Long id);
}
