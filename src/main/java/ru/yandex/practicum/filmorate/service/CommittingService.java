package ru.yandex.practicum.filmorate.service;

public interface CommittingService<T> {
    T create(T t);

    T update(T t);
}
