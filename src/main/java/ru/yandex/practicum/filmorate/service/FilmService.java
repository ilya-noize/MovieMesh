package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService extends Service<Film> {
    List<Film> getPopular(Long supposedCount);

    void addLike(Long supposedId, Long supposedUsedId);

    void deleteLike(Long supposedId, Long supposedUsedId);
}
