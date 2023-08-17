package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.dao.rowMapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dao.rowMapper.MPARatingRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresFilm;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
public class FilmService extends MasterService<Film> {
    private static Long filmId;
    private final MasterStorageDAO<GenresFilm> genresFilmMasterStorageDAO;
    private final MasterStorageDAO<Genre> genreStorage;
    private final Comparator<Film> sortLikes = Comparator.comparing(film -> film.getLikes().size());

    public FilmService(MasterStorageDAO<Film> storage,
                       MasterStorageDAO<GenresFilm> genresFilmMasterStorageDAO, MasterStorageDAO<Genre> genreStorage) {
        super(storage);
        this.genresFilmMasterStorageDAO = genresFilmMasterStorageDAO;
        this.genreStorage = genreStorage;
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
    public Film update(Film film) {
        log.info("[i] service: update film:{}", film);
        filmId = film.getId();
        super.isExist(filmId);
        super.update(film);
        Set<Genre> genres = film.getGenres();
        updateGenresFilm(
                filterAllGenresFilms(genresFilmMasterStorageDAO.getAll()),
                getSetGenreIds(genres)
        );
        return get(filmId);
    }

    @Override
    public Film get(Long id) {
        Film film = super.get(id);
        filmId = film != null ? film.getId() : null;
        if (filmId != null) {
            Set<Long> likes = getUserLikes();

            film.setMpa(getMPA());
            film.setRate(likes.size());
            film.setGenres(getGenresByFilm());
            film.setLikes(likes);
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        Set<Long> idFilms = super.getAll().stream()
                .map(Film::getId)
                .collect(toSet());

        return idFilms.stream()
                .map(this::get)
                .collect(toList());
    }

    private MPARating getMPA() {
        String sql = "SELECT MPA.* FROM MPA_RATING MPA"
                + " RIGHT JOIN FILMS F ON F.MPA_RATING_ID = MPA.ID"
                + " WHERE F.ID = ?;";
        return genresFilmMasterStorageDAO.getJdbcTemplate().queryForObject(
                sql,
                new MPARatingRowMapper(),
                filmId
        );
    }

    private Set<Genre> getGenresByFilm() {
        String sql = "SELECT G.* FROM GENRES_FILM GF "
                + "RIGHT JOIN FILMS F ON F.id = GF.FILM_ID "
                + "RIGHT JOIN GENRES G ON G.id = GF.GENRE_ID "
                + "WHERE F.ID = ? "
                + "ORDER BY G.ID";
        return new HashSet<>(genreStorage.getJdbcTemplate().query(
                sql,
                new GenreRowMapper(),
                filmId)
        );
    }

    private Set<Long> getUserLikes() {
        String sql = "SELECT user_id FROM films_like WHERE film_id = ?";
        return new HashSet<>(genresFilmMasterStorageDAO.getJdbcTemplate().query(
                sql,
                (rs, rowNum) -> rs.getLong("user_id"),
                filmId));
    }

    private Set<Long> filterAllGenresFilms(List<GenresFilm> getAll) {
        log.info("[i] service: update: filterAllGenresFilms");
        if (getAll == null) {
            return new HashSet<>();
        }
        return getAll.stream()
                .filter(genresFilm -> genresFilm.getFilmId().equals(filmId))
                .map(GenresFilm::getId)
                .collect(toSet());
    }

    private Set<Long> getSetGenreIds(Set<Genre> genres) {
        log.info("[i] service: update: getSetGenreIds");
        if (genres == null) {
            return new HashSet<>();
        }
        return genres.stream()
                .map(Genre::getId)
                .collect(toSet());
    }

    /**
     * If there are more movie genre data records
     * in the database than the incoming data,
     * then delete the record that is not in the incoming data.
     * Otherwise, add data that is not in the database.
     * <hr>
     * In other words, look for a difference in the incoming data.
     * Based on the logic of the priority of the incoming request,
     * make changes in the database, namely, removing excess or adding missing.
     *
     * @param genresFilmOn Data genres of movie
     * @param genresFilmIn Incoming genres of movie
     */
    private void updateGenresFilm(Set<Long> genresFilmOn,
                                  Set<Long> genresFilmIn) {
        log.info("[i] service: update: updateGenresFilm id:{}", filmId);

        if (genresFilmOn.size() > genresFilmIn.size()) {
            genresFilmOn.retainAll(genresFilmIn);
            deleteGenres(genresFilmOn);
        }
        if (genresFilmOn.size() < genresFilmIn.size()) {
            genresFilmIn.retainAll(genresFilmOn);
            addGenres(genresFilmIn);
        }
    }

    private void deleteGenres(Set<Long> genresFilmOn) {
        log.info("[i] service: update: deleteGenres genresFilmOn:{}",
                genresFilmOn);
        genresFilmOn.forEach(
                genresFilm -> genresFilmMasterStorageDAO.delete(
                        filmId, genresFilm
                )
        );
    }

    private void addGenres(Set<Long> genresFilmIn) {
        genresFilmIn.forEach(
                genresId -> genresFilmMasterStorageDAO.create(
                        new GenresFilm(filmId, genresId)
                )
        );

    }
}
