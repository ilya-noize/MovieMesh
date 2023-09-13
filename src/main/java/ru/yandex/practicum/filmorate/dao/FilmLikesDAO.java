package ru.yandex.practicum.filmorate.dao;

public interface FilmLikesDAO {
    void add(Long filmId, Long userId);

    void delete(Long filmId, Long userId);
}
