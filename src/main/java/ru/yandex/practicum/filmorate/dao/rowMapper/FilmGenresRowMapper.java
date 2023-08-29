package ru.yandex.practicum.filmorate.dao.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenres;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
public class FilmGenresRowMapper implements RowMapper<FilmGenres> {
    @Override
    public FilmGenres mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FilmGenres(rs.getLong("film_id"),
                new Genre(rs.getLong("genre_id"),
                        rs.getString("genre_name"))
        );
    }
}
