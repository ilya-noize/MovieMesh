package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.rowMapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@Primary
public final class GenreDAO extends MasterStorageDAO<Genre> {
    public GenreDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Genre create(Genre genre) {
        genre.setId(increment());
        getJdbcTemplate().update("INSERT INTO genres (genre)"
                        + " VALUES (?) ON CONFLICT DO NOTHING",
                genre.getName());
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        getJdbcTemplate().update("UPDATE genres SET genre = ?"
                        + " WHERE id = ?",
                genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public Genre get(Long id) {
        String error = String.format("Genre not found - id:%d not exist", id);
        String sql = "SELECT * FROM genres WHERE id = ?";

        return getJdbcTemplate().query(sql, this::make, id)
                .stream().findFirst()
                .orElseThrow(new NotFoundException(error));
    }

    @Override
    public void delete(Long... id) {
        String sql = "DELETE FROM genres WHERE id = ?;";
        getJdbcTemplate().update(sql, id[0]);
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM genres ORDER BY id";
        return getJdbcTemplate().query(sql, this::make);
    }

    @Override
    public Genre make(ResultSet rs, int rowNum) throws SQLException {
        return new GenreRowMapper().mapRow(rs, rowNum);
    }
}
