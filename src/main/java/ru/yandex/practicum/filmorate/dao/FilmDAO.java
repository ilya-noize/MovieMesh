package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.rowMapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dao.rowMapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.dao.rowMapper.MPARatingRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Primary
public final class FilmDAO extends MasterStorageDAO<Film> {
    @Autowired
    public FilmDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Film create(Film film) {
        film.setId(increment());
        String sql = "INSERT INTO films"
                + " (name, description, duration, releaseDate, mpa_rating_id)"
                + " VALUES (?, ?, ?, ?, ?)";
        getJdbcTemplate().update(sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET"
                + " name = ?, description = ?, duration = ?, releaseDate = ?, mpa_rating_id=?"
                + " WHERE id = ?;";
        getJdbcTemplate().update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public Film get(Long id) {
        String error = String.format("Film not found - id:%d not exist", id);
        String sql = "SELECT F.* FROM films F WHERE F.id = ? ORDER BY F.id";
        return getJdbcTemplate().query(sql, this::make, id)
                .stream().findFirst()
                .orElseThrow(new NotFoundException(error));
    }

    @Override
    public void delete(Long... id) {
        String sql = "DELETE FROM films F WHERE id = ?;";
        getJdbcTemplate().update(sql, id[0]);
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT F.* FROM films F"
                + " ORDER BY F.ID;";
        return getJdbcTemplate().query(sql, this::make);
    }

    @Override
    public Film make(ResultSet rs, int rowNum) throws SQLException {
        Film film = new FilmRowMapper().mapRow(rs, rowNum);
        Long filmId = film != null ? film.getId() : null;
        if (filmId != null) {
            Set<Long> likes = getUserLikes(filmId);

            film.setMpa(getMPA(filmId));
            film.setRate(likes.size());
            film.setGenres(getGenresByFilm(filmId));
            film.setLikes(likes);
        }
        return film;
    }

    private MPARating getMPA(Long filmId) {
        String sql = "SELECT MPA.* FROM MPA_RATING MPA"
                + " RIGHT JOIN FILMS F ON F.MPA_RATING_ID = MPA.ID"
                + " WHERE F.ID = ?;";
        return getJdbcTemplate().queryForObject(sql, new MPARatingRowMapper(), filmId);
    }

    private Set<Genre> getGenresByFilm(Long id) {
        String sql = "SELECT G.* FROM GENRES_FILM GF "
                + "RIGHT JOIN FILMS F ON F.id = GF.FILM_ID "
                + "RIGHT JOIN GENRES G ON G.id = GF.GENRE_ID "
                + "WHERE F.ID = ? "
                + "ORDER BY G.ID";
        return new HashSet<>(getJdbcTemplate().query(sql, new GenreRowMapper(), id));
    }

    private Set<Long> getUserLikes(Long id) {
        String sql = "SELECT user_id FROM films_like WHERE film_id = ?";
        return new HashSet<>(getJdbcTemplate().query(
                sql,
                (rs, rowNum) -> rs.getLong("user_id"),
                id));
    }
}
