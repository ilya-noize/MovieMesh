package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.FilmGenres;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmGenresDAO {
    void add(Long filmId, List<Genre> genres);

    void update(Long filmId, List<Genre> genres);

    Map<Long, List<Genre>> getFilmGenres(Set<Long> filmIds);

    List<FilmGenres> getAllFilmGenres(Set<Long> filmIds);
}
