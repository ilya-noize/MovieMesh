package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.rowMapper.GenresFilmRowMapper;
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
        String sql = "MERGE INTO genres_film KEY(film_id, genre_id) VALUES (? , ?)";
        getJdbcTemplate().update(sql, genresFilm.getFilmId(), genresFilm.getGenreId());
        return genresFilm;
    }

    @Override
    public GenresFilm update(GenresFilm genresFilm) {
        return null;
    }

    @Override
    public GenresFilm get(Long id) {
        return null;
    }

    @Override
    public void delete(Long... id) {
        log.info("[i] deleteGenreFilm\n ids:{}", (Object[]) id);
        Long filmId = id[0];
        Long genreId = id[1];
        String sql = "DELETE FROM genres_film WHERE film_id = ?";
        if (genreId == null) {
            getJdbcTemplate().update(sql, filmId);
        } else {
            sql += " AND genre_id = ?";
            getJdbcTemplate().update(sql, filmId, genreId);
        }
    }

    @Override
    public List<GenresFilm> getAll() {
        String sql = "SELECT film_id, genre_id FROM genres_film GROUP BY film_id, genre_id";
        return getJdbcTemplate().query(sql, this::make);
    }

    @Override
    public GenresFilm make(ResultSet rs, int rowNum) throws SQLException {
        return new GenresFilmRowMapper().mapRow(rs, rowNum);
    }
}
