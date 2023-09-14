package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService extends CommittingService<Film>, ShowableService<Film> {
    List<Film> getPopular(Long count);

    void isExist(Long id);
}
