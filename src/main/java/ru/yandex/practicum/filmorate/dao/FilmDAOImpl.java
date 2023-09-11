package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
@Primary
@RequiredArgsConstructor
public final class FilmDAOImpl implements FilmDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Film> filmRowMapper;

    @Override
    public Film create(Film film) {
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
        return new Film(
                filmId,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa(),
                film.getGenres()
        );
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET"
                + " name = ?, description = ?, duration = ?, releasedate = ?, mpa_rating_id=?"
                + " WHERE id = ?;";
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

    @Override
    public Film get(Long id) {
        String error = String.format("Film not found - id:%d not exist", id);
        String sql = "SELECT F.id FILM_ID,"
                + " F.name FILM_NAME,"
                + " F.description FILM_DESCRIPTION,"
                + " F.duration FILM_DURATION,"
                + " F.releasedate FILM_RELEASE,"
                + " MPA.id MPA_ID,"
                + " MPA.name MPA_NAME,"
                + " MPA.description MPA_DESCRIPTION "
                + "FROM mpa_rating MPA "
                + "RIGHT JOIN films F ON F.mpa_rating_id = MPA.id "
                + "WHERE F.id = ?";
        return jdbcTemplate.query(sql, filmRowMapper, id)
                .stream().findFirst()
                .orElseThrow(new NotFoundException(error));
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT F.id FILM_ID,"
                + " F.name FILM_NAME,"
                + " F.description FILM_DESCRIPTION,"
                + " F.duration FILM_DURATION,"
                + " F.releasedate FILM_RELEASE,"
                + " MPA.id MPA_ID,"
                + " MPA.name MPA_NAME,"
                + " MPA.description MPA_DESCRIPTION "
                + "FROM mpa_rating MPA "
                + "RIGHT JOIN films F ON F.mpa_rating_id = MPA.id "
                + "ORDER BY F.id, MPA.id;";
        return jdbcTemplate.query(sql, filmRowMapper);
    }

    @Override
    public List<Film> getPopular(Long count) {
        String sql = "SELECT F.id FILM_ID,"
                + " F.name FILM_NAME,"
                + " F.description FILM_DESCRIPTION,"
                + " F.duration FILM_DURATION,"
                + " F.releasedate FILM_RELEASE,"
                + " MPA.id MPA_ID,"
                + " MPA.name MPA_NAME,"
                + " MPA.description MPA_DESCRIPTION,"
                + " COUNT(FL.USER_ID) LIKES "
                + "FROM films F "
                + "LEFT JOIN mpa_rating MPA ON F.mpa_rating_id = MPA.id "
                + "LEFT JOIN films_like FL ON FL.film_id = F.id "
                + "GROUP BY F.id "
                + "ORDER BY LIKES DESC LIMIT ?;";
        return jdbcTemplate.query(sql, filmRowMapper, count);
    }
}
