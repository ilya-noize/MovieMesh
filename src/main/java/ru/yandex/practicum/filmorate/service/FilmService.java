package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage){
        this.filmStorage = filmStorage;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film get(Integer id) {
        return filmStorage.get(id);
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public void like(Integer filmId, Integer userId) {
        filmStorage.like(filmId, userId);
    }

    public void unLike(Integer filmId, Integer userId) {
        filmStorage.unLike(filmId, userId);
    }

    public List<Film> getPopular(Integer count) {
        return filmStorage.getPopular(count);
    }
}