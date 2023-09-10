package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.rowMapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
@Primary
@RequiredArgsConstructor
public final class FilmDAO {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    public Film create(Film film) {
        film.setId(null);
        String sql = "INSERT INTO films (name, description, duration, releaseDate, mpa_rating_id)"
                + " VALUES (?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setInt(3, film.getDuration());
            ps.setDate(4, Date.valueOf(film.getReleaseDate()));
            ps.setLong(5, film.getMpa().getId());
            return ps;
        };
        jdbcTemplate.update(psc, keyHolder);
        Long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(filmId);
        return film;
    }

    public Film update(Film film) {
        String sql = "UPDATE FILMS SET"
                + " NAME = ?, DESCRIPTION = ?, DURATION = ?, RELEASEDATE = ?, MPA_RATING_ID=?"
                + " WHERE ID = ?;";
        jdbcTemplate.update(
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

    public Film get(Long id) {
        String error = String.format("Film not found - id:%d not exist", id);
        String sql = "SELECT F.* FROM films F WHERE F.id = ? ORDER BY F.id";
        return jdbcTemplate.query(sql, filmRowMapper, id)
                .stream().findFirst()
                .orElseThrow(new NotFoundException(error));
    }

    public List<Film> getAll() {
        String sql = "SELECT F.* FROM films F"
                + " ORDER BY F.ID;";
        return jdbcTemplate.query(sql, filmRowMapper);
    }

    public List<Film> getPopular(Long count) {
        String sql = "SELECT F.*, COUNT(FL.USER_ID) LIKES FROM FILMS_LIKE FL" +
                " RIGHT JOIN FILMS F ON FL.FILM_ID = F.ID" +
                " GROUP BY F.ID " +
                " ORDER BY LIKES DESC LIMIT ?;";
        return jdbcTemplate.query(sql, filmRowMapper, count);
    }
}
