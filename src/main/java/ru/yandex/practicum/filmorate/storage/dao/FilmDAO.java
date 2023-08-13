package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilmDAO extends MasterStorage<Film> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        film.setId(increment());
        String sql = "INSERT INTO 'FILMS' (id, name, description, duration, release, mpa_rating_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, film.getId(), film.getName(), film.getDescription(), film.getDuration(), film.getRelease(), film.getMpaRatingId());
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE 'FILMS' SET name = ?, description = ?, duration = ?, release = ?, mpaRatingId=? WHERE id = ?;";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getDuration(), film.getRelease(), film.getMpaRatingId(), film.getId());
        return film;
    }

    @Override
    public Film get(Long id) {
        String sql = "SELECT * FROM 'FILMS' WHERE 'id' = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper<>(Film.class)).stream().findFirst().orElse(null);
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM 'FILMS'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Film.class));
    }

    @Override
    public boolean isExist(Long id) {
        return false;
    }
}
