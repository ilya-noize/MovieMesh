package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class MasterService<T> {
    private final MasterStorageDAO<T> storage;

    public T create(T t) {
        return storage.create(t);
    }

    public T update(T t) {
        return storage.update(t);
    }

    public T get(Long id) {
        return storage.get(id);
    }

    public void delete(Long... id) {
        storage.delete(id);
    }

    public List<T> getAll() {
        return storage.getAll();
    }

    public boolean isExist(Long id) {
        return storage.isExist(id);
    }
}
