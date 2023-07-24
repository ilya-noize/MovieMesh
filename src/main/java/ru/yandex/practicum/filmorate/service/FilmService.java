package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film create(Film film);

    Film update(Film film);

    Film get(Long supposedId);

    List<Film> getAll();

    List<Film> getPopular(Long supposedCount);

    void addLike(Long supposedId, Long supposedUsedId);

    void deleteLike(Long supposedId, Long supposedUsedId);
}
