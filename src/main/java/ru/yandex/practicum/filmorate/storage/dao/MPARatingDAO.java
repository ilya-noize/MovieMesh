package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MPARatingDAO extends MasterStorage<MPARating> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public MPARating create(MPARating mpaRating) {
        mpaRating.setId(increment());
        jdbcTemplate.update("INSERT INTO 'MPA_RATING' (id, rating, description) VALUES (?, ?, ?)",
                mpaRating.getId(), mpaRating.getRating(), mpaRating.getDescription());
        return mpaRating;
    }

    @Override
    public MPARating update(MPARating mpaRating) {
        jdbcTemplate.update("UPDATE 'MPA_RATING' SET rating = ?, description = ? WHERE id = ?",
                mpaRating.getRating(), mpaRating.getDescription(), mpaRating.getId());
        return mpaRating;
    }

    @Override
    public MPARating get(Long id) {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING WHERE 'id' = ?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(MPARating.class))
                .stream().findFirst()
                .orElse(null);
    }

    @Override
    public List<MPARating> getAll() {
        return jdbcTemplate.query("SELECT * FROM MPA_RATING", new BeanPropertyRowMapper<>(MPARating.class));
    }

    @Override
    public boolean isExist(Long id) {
        return false;
    }
}
