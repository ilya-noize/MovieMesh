package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constraint.CorrectReleaseDate;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.dao.rowMapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresFilm;
import ru.yandex.practicum.filmorate.model.MPARating;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.yandex.practicum.filmorate.model.Film.RELEASE_DATE_LIMIT;

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
        film = super.create(valid(film));
        filmId = film.getId();
        List<Genre> genres = film.getGenres();
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
        List<Genre> genresId = film.getGenres();
        log.info("[u] Film Service: \n film:{}\n genresId:{}", film, genresId);
        super.isExist(filmId);
        super.update(film);
        if (genresId != null) {
            log.info("[i] service: update genresId:{}", genresId);
            genresFilmStorage.delete(filmId, null);
            genresId.forEach(genreId -> {
                GenresFilm genresFilm = new GenresFilm(filmId, genreId.getId());//genreId);
                genresFilmStorage.create(genresFilm);
            });
        }
        return get(filmId);
    }

    @Override
    public Film get(Long id) {
        Film film = super.get(id);
        log.info("[>] FilmService\ngetFilm(id = {})\nFilm = {}", id, film);
        filmId = film != null ? film.getId() : null;
        if (filmId != null) {
            List<Long> likes = getUserLikes();

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

    private List<Genre> getGenresByFilm() {
        String sql = "SELECT G.ID, G.NAME FROM GENRES_FILM GF"
                + " RIGHT JOIN FILMS F ON F.id = GF.FILM_ID"
                + " RIGHT JOIN GENRES G ON G.id = GF.GENRE_ID"
                + " WHERE F.ID = ? ORDER BY G.ID, G.NAME;";
        return genresFilmStorage.getJdbcTemplate().query(sql, new GenreRowMapper(), filmId);
    }

    private List<Long> getUserLikes() {
        String sql = "SELECT user_id FROM films_like WHERE film_id = ? ORDER BY user_id";
        return genresFilmStorage.getJdbcTemplate().query(sql,
                (rs, rowNum) -> rs.getLong("user_id"),
                filmId);
    }

    private Film valid(Film film) {
        film.setName(checkName(film));
        film.setReleaseDate(checkRelease(film));
        film.setDescription(checkDecriprtion(film));
        film.setDuration(checkDuration(film));
        return film;
    }

    @NotNull(message = "The name of the movie cannot be null.")
    @NotBlank(message = "The title of the movie cannot be blank.")
    private String checkName(Film film) {
        return film.getName();
    }

    @CorrectReleaseDate(value = RELEASE_DATE_LIMIT, message = "The release date is not earlier than a certain date and not later than today")
    private LocalDate checkRelease(Film film) {
        return film.getReleaseDate();
    }

    @Size(max = 200, message = "The description length is no more than 200 characters.")
    private String checkDecriprtion(Film film) {
        return film.getDescription();
    }

    @Positive(message = "The duration of the movie is a positive natural number.")
    private Integer checkDuration(Film film) {
        return film.getDuration();
    }
}
