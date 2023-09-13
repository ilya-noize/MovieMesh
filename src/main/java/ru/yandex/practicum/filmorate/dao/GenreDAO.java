package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
@RequiredArgsConstructor
public final class GenreDAO implements Showable<Genre> {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Genre> genreRowMapper;

    @Override
    public Genre get(Long id) {
        String error = String.format("Genre not found - id:%d not exist", id);
        String sql = "SELECT * FROM GENRES WHERE ID = ?";

        return jdbcTemplate.query(sql, genreRowMapper, id)
                .stream().findFirst()
                .orElseThrow(() -> new NotFoundException(error));
    }

    @Override
    public List<Genre> getAll() {
        String sql = "SELECT * FROM GENRES ORDER BY ID";
        return jdbcTemplate.query(sql, genreRowMapper);
    }
}
