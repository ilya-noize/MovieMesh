package ru.yandex.practicum.filmorate.dao.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("id");
        String name = rs.getString("Name");
        LocalDate releaseDate = rs.getDate("ReleaseDate").toLocalDate();
        String description = rs.getString("Description");
        Integer duration = rs.getInt("Duration");
        MPARating mpa = new MPARating(rs.getLong("mpa_rating_id"), null, null);
        List<Genre> genres = null;

        return new Film(filmId, name, description, releaseDate, duration, mpa, genres);
    }
}
