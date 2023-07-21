package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film create(Film film);

    Film update(Film film);

    Film get(String supposedId);

    List<Film> getAll();

    List<Film> getPopular(String supposedCount);

    void addLike(String supposedId, String supposedUsedId);

    void deleteLike(String supposedId, String supposedUsedId);
}
