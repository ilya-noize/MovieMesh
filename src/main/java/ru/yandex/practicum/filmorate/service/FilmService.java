package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.dao.FilmGenresDAO;
import ru.yandex.practicum.filmorate.dao.GenreDAO;
import ru.yandex.practicum.filmorate.dao.MPARatingDAO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private static Long filmId;
    private final FilmDAO filmDAO;
    private final FilmGenresDAO filmGenresDAO;
    private final GenreDAO genreDAO;
    private final MPARatingDAO mpaRatingDAO;

    public Film create(Film film) {
        log.info("[+][S] Film: \n film:{}", film);
        filmDAO.create(film);
        filmId = film.getId();
        List<Genre> genres = film.getGenres();
        if (genres != null) {
            filmGenresDAO.add(filmId, genres);
        }
        return get(filmId);
    }

    public Film update(Film film) {
        filmId = film.getId();
        List<Genre> genres = film.getGenres();
        log.info("[u][S] Film: \n film:{}\n genres:{}", film, genres);
        isExist(filmId);
        filmDAO.update(film);
        if (genres != null) {
            log.info("[i] service: update genres:{}", genres);
            filmGenresDAO.delete(filmId);
            filmGenresDAO.add(filmId, genres);
        }
        return get(filmId);
    }

    public Film get(Long id) {
        filmId = id;
        Film film = filmDAO.get(filmId);
        log.info("[>][S] Film\nFilm = {}", film);
        film.setMpa(mpaRatingDAO.get(film.getMpa().getId()));

        Map<Long, List<Genre>> genres = genreDAO.getFilmGenres(Set.of(filmId));
        if (genres.isEmpty()) {
            film.setGenres(new ArrayList<>());
        } else {
            film.setGenres(genres.get(filmId));
        }
        return film;
    }

    public List<Film> getAll() {
        List<Film> films = filmDAO.getAll();
        log.info("[>][S] All Films (size = {})", films.size());
        return films;
    }

    /**
     * Возвращает список популярных фильмов у пользователей
     *
     * @param count количество фильмов
     * @return список
     */
    public List<Film> getPopular(Long count) {
        log.info("Returning the TOP-{} popular movies from users", count);
        return filmDAO.getPopular(count);
    }

    private void isExist(Long... ids) {
        for (Long id : ids) {
            get(id);
        }
    }
}
