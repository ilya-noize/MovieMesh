package ru.yandex.practicum.filmorate.dao.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.GenresFilm;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenresFilmRowMapper implements RowMapper<GenresFilm> {
    @Override
    public GenresFilm mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new GenresFilm(rs.getLong("film_id"), rs.getLong("genre_id"));
    }
}
