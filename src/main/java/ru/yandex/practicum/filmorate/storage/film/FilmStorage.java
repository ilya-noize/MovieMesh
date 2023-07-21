package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {
    Film create(Film film);

    Film update(Film film);

    Film get(Integer id);

    List<Film> getAll();

    void setLikes(Integer filmId, Set<Integer> userLikes);

    Set<Integer> getLikes(Integer filmId);
}
