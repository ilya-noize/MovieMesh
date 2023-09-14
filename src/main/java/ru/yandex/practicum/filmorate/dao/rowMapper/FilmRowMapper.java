package ru.yandex.practicum.filmorate.dao.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("FILM_ID");
        String name = rs.getString("FILM_NAME");
        LocalDate release = rs.getDate("FILM_RELEASE").toLocalDate();
        String description = rs.getString("FILM_DESCRIPTION");
        Integer duration = rs.getInt("FILM_DURATION");
        MPARating mpa = new MPARating(
                rs.getLong("MPA_ID"),
                rs.getString("MPA_NAME"),
                rs.getString("MPA_DESCRIPTION")
        );

        return new Film(filmId, name, description, release, duration, mpa, null);
    }
}
