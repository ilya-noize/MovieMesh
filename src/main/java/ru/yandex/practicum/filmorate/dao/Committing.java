package ru.yandex.practicum.filmorate.dao;

public interface Committing<T> {
    T create(T t);

    T update(T t);
}
