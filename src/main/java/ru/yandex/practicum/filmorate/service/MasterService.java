package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public abstract class MasterService<T> {
    private final MasterStorage<T> storage;

    public T create(T t) {
        return storage.create(t);
    }

    public T update(T t) {
        return storage.update(t);
    }

    public T get(Long id) {
        return storage.get(id);
    }

    public List<T> getAll() {
        return storage.getAll();
    }
}
