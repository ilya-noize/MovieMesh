package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Film ifExist(Integer id);

    Film get(Integer id);

    List<Film> getAll();

    void like(Integer filmId, Integer userId);

    void unLike(Integer filmId, Integer userId);

    List<Film> getPopular(Integer count);
}
