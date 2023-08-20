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
        String sql = "INSERT INTO genres_film (film_id, genre_id) VALUES (?, ?)";
        getJdbcTemplate().update(sql, genresFilm.getFilmId(), genresFilm.getGenreId());
        return genresFilm;
    }

    /**
     * если жанр есть в базе, то удаление
     * если жанра нет в базе, то создание
     *
     * @param genresFilm Жанр фильма
     * @return Жанр фильма
     */
    @Override
    public GenresFilm update(GenresFilm genresFilm) {
        log.info("[i] update\n GenresFilm:{}", genresFilm);
        /*
        Long filmId = genresFilm.getFilmId();
        Long genreId = genresFilm.getGenreId();

        delete(filmId, genreId);*/
        return null;/*create(genresFilm);*/
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
