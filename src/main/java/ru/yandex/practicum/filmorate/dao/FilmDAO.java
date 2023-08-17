package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.rowMapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
        String sql = "INSERT INTO films"
                + " (name, description, duration, releaseDate, mpa_rating_id)"
                + " VALUES (?, ?, ?, ?, ?)";
        getJdbcTemplate().update(sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId());
        film.setId(increment());
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
        return new FilmRowMapper().mapRow(rs, rowNum);
    }
}
