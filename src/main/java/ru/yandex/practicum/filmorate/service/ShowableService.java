package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface ShowableService<T> {
    T get(Long id);

    List<T> getAll();
}
