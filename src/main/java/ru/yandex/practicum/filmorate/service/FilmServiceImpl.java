package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.dao.FilmGenresDAO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private static Long filmId;
    private final FilmDAO filmDAO;
    private final FilmGenresDAO filmGenresDAO;

    public Film create(Film film) {
        log.debug("[+][S] Film: \n film:{}", film);
        film = filmDAO.create(film);
        filmId = film.getId();
        LinkedHashSet<Genre> genres = film.getGenres();
        if (genres != null) {
            filmGenresDAO.add(filmId, new ArrayList<>(genres));
        }
        return get(filmId);
    }

    public Film update(Film film) {
        isExist(film.getId());
        filmId = film.getId();
        LinkedHashSet<Genre> genres = film.getGenres();
        log.debug("[u][S] Film: \n film:{}\n genres:{}", film, genres);
        filmDAO.update(film);
        if (genres != null) {
            log.debug("[i] service: update genres:{}", genres);
            filmGenresDAO.update(filmId, new ArrayList<>(genres));
        }
        return get(filmId);
    }

    public Film get(Long id) {
        filmId = id;
        Film film = filmDAO.get(filmId);
        log.info("[>][S] Film\nFilm = {}", film);

        Map<Long, List<Genre>> genres = filmGenresDAO.getFilmGenres(Set.of(filmId));
        if (genres.isEmpty()) {
            film.setGenres(new LinkedHashSet<>());
        } else {
            film.setGenres(new LinkedHashSet<>(genres.get(filmId)));
        }
        return film;
    }

    public List<Film> getAll() {
        List<Film> films = filmDAO.getAll();
        log.debug("[>][S] All Films (size = {})", films.size());

        if (films.isEmpty()) {
            return new ArrayList<>();
        }
        return getAllDataFilm(films);
    }

    /**
     * Возвращает список популярных фильмов у пользователей
     *
     * @param count количество фильмов
     * @return список
     */
    public List<Film> getPopular(Long count) {
        log.debug("Returning the TOP-{} popular movies from users", count);
        List<Film> films = filmDAO.getPopular(count);
        if (films.isEmpty()) {
            return new ArrayList<>();
        }
        return getAllDataFilm(films);
    }

    @Override
    public void isExist(Long id) {
        if (filmDAO.isExist(id).equals(0L)) {
            String error = String.format("Film not found - id:%d not exist", id);
            log.error(error);
            throw new NotFoundException(error);
        }
    }

    private List<Film> getAllDataFilm(List<Film> films) {
        Set<Long> filmIds = films.stream()
                .map(Film::getId)
                .collect(toSet());
        Map<Long, List<Genre>> genres = filmGenresDAO.getFilmGenres(filmIds);
        films.forEach(film -> {
            if (genres.isEmpty()) {
                film.setGenres(new LinkedHashSet<>());
            } else {
                film.setGenres(new LinkedHashSet<>(genres.get(film.getId())));
            }
        });
        return films;
    }
}
