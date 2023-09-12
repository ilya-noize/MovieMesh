package ru.yandex.practicum.filmorate.service;

public interface LikeService {
    void add(Long filmId, Long userId);

    void delete(Long filmId, Long userId);
}
