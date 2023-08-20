package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresFilm;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class FilmService extends MasterService<Film> {
    private static Long filmId;
    private final MasterStorageDAO<GenresFilm> genresFilmStorage;
    private final MasterStorageDAO<Genre> genreStorage;
    private final MasterStorageDAO<MPARating> mpaRatingStorage;
    private final Comparator<Film> sortLikes = Comparator.comparing(film -> film.getLikes().size());

    public FilmService(MasterStorageDAO<Film> storage,
                       MasterStorageDAO<GenresFilm> genresFilmStorage,
                       MasterStorageDAO<Genre> genreStorage,
                       MasterStorageDAO<MPARating> mpaRatingStorage) {
        super(storage);
        this.genresFilmStorage = genresFilmStorage;
        this.genreStorage = genreStorage;
        this.mpaRatingStorage = mpaRatingStorage;
    }

    /**
     * Возвращает список популярных фильмов у пользователей
     *
     * @param supposedCount количество фильмов
     * @return список
     */
    public List<Film> getPopular(Long supposedCount) {
        log.info("Returning the TOP{} popular movies from users", supposedCount);
        return this.getAll().stream()
                .sorted(sortLikes.reversed())
                .limit(supposedCount)
                .collect(toList());
    }

    @Override
    public Film create(Film film) {
        film.setId(super.create(film).getId());
        filmId = film.getId();
        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            genres.forEach(genre ->
                    genresFilmStorage.create(
                            new GenresFilm(filmId, genre.getId())
                    ));
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        filmId = film.getId();
        Set<Genre> genres = film.getGenres();
        log.info("[i] Film Service: update\n film:{}\n genres:{}", film, genres);
        super.isExist(filmId);
        super.update(film);
        if (genres != null) {
            log.info("[i] service: update genres:{}", genres);
            genresFilmStorage.delete(filmId, null);
            genres.forEach(genre -> {
                GenresFilm genresFilm = new GenresFilm(filmId, genre.getId());
                genresFilmStorage.create(genresFilm);
            });
        }

        return film;
    }

    @Override
    public Film get(Long id) {
        Film film = super.get(id);
        log.info("[i] FilmService\ngetFilm(id = {})\nFilm = {}", id, film);
        filmId = film != null ? film.getId() : null;
        if (filmId != null) {
            Set<Long> likes = getUserLikes();

            film.setMpa(mpaRatingStorage.get(film.getMpa().getId()));
            film.setRate(likes.size());
            film.setGenres(getGenresByFilm());
            film.setLikes(likes);
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        return super.getAll().stream()
                .map(Film::getId)
                .map(this::get)
                .collect(toList());
    }

    private Set<Genre> getGenresByFilm() {
        Set<Long> allGenres = genreStorage.getAll().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        Set<Long> genresFilmSet = genresFilmStorage.getAll().stream()
                .filter(genresFilm -> genresFilm.getFilmId().equals(filmId))
                .map(GenresFilm::getGenreId)
                .collect(Collectors.toSet());

        return allGenres.stream()
                .filter(genresFilmSet::contains)
                .map(genreStorage::get)
                .collect(Collectors.toSet());
    }

    private Set<Long> getUserLikes() {
        String sql = "SELECT user_id FROM films_like WHERE film_id = ? ORDER BY user_id";
        return new HashSet<>(genresFilmStorage.getJdbcTemplate().query(
                sql,
                (rs, rowNum) -> rs.getLong("user_id"),
                filmId)
        );
    }
}
