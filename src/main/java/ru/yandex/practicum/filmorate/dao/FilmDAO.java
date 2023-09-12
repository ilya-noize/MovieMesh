package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmDAO extends Showable<Film>, Committing<Film> {
    List<Film> getPopular(Long count);

    Long isExist(Long id);
}
