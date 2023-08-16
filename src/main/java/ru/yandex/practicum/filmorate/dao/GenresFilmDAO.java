package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.GenresFilm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@Primary
public final class GenresFilmDAO extends MasterStorageDAO<GenresFilm> {
    @Autowired
    public GenresFilmDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public GenresFilm create(GenresFilm genresFilm) {
        String sql = "INSERT INTO genres_film (film_id, genre_id) VALUES (?, ?)";
        getJdbcTemplate().update(sql, genresFilm.getFilmId(), genresFilm.getId());
        return genresFilm;
    }

    @Override
    public GenresFilm update(GenresFilm genresFilm) {
        String sql = "UPDATE genres_film SET genre_id = ? WHERE film_id = ?;";
        getJdbcTemplate().update(sql, genresFilm.getId(), genresFilm.getFilmId());
        return genresFilm;
    }

    @Override
    public GenresFilm get(Long id) {
        return null;
    }

    @Override
    public void delete(Long... id) {
        String sql = "DELETE FROM genres_film WHERE film_id = ? AND genre_id = ?;";
        getJdbcTemplate().update(sql, id[0], id[1]);
    }

    @Override
    public List<GenresFilm> getAll() {
        String sql = "SELECT * FROM genres_film";
        return getJdbcTemplate().query(sql, this::make);
    }

    @Override
    public GenresFilm make(ResultSet rs, int rowNum) throws SQLException {
        return new GenresFilm(rs.getLong("film_id"), rs.getLong("genre_id"));
    }
}
