package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public final class FilmLikesDAO {
    private final JdbcTemplate jdbcTemplate;

    public void add(Long filmId, Long userId) {
        String sql = "MERGE INTO FILMS_LIKE KEY(FILM_ID, USER_ID) VALUES (?, ?);";
        jdbcTemplate.update(sql, filmId, userId);
    }

    public void delete(Long filmId, Long userId) {
        String sql = "DELETE FROM FILMS_LIKE WHERE FILM_ID = ? AND USER_ID = ?;";
        jdbcTemplate.update(sql, filmId, userId);
    }
}
