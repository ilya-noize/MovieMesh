package ru.yandex.practicum.filmorate.dao;

import java.util.List;

public interface Showable<T> {
    T get(Long id);

    List<T> getAll();
}
