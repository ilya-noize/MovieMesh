package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.rowMapper.MPARatingRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public final class MPARatingDAO {
    private final JdbcTemplate jdbcTemplate;
    private final MPARatingRowMapper mpaRatingRowMapper;

    public MPARating get(Long id) {
        String sql = "SELECT * FROM mpa_rating WHERE id = ?";
        String error = String.format("MPARating not found - id:%d not exist", id);
        return jdbcTemplate.query(sql, mpaRatingRowMapper, id)
                .stream().findFirst()
                .orElseThrow(new NotFoundException(error));
    }

    public List<MPARating> getAll() {
        String sql = "SELECT * FROM mpa_rating";
        return jdbcTemplate.query(sql, mpaRatingRowMapper);
    }
}
