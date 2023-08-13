package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDAO extends MasterStorage<Genre> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre create(Genre genre) {
        genre.setId(increment());
        jdbcTemplate.update("INSERT INTO 'GENRES' (id, genre) VALUES (?, ?)",
                genre.getId(), genre.getGenre());
        return genre;
    }

    @Override
    public Genre update(Genre genre) {
        jdbcTemplate.update("UPDATE 'GENRES' SET genre = ? WHERE id = ?",
                genre.getGenre(), genre.getId());
        return genre;
    }

    @Override
    public Genre get(Long id) {
        return jdbcTemplate.query("SELECT * FROM 'GENRES' WHERE 'id' = ?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(Genre.class))
                .stream().findFirst()
                .orElse(null);
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query("SELECT * FROM 'GENRES'", new BeanPropertyRowMapper<>(Genre.class));
    }

    @Override
    public boolean isExist(Long id) {
        return false;
    }
}
