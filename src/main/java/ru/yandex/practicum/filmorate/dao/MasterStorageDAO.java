package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
public abstract class MasterStorageDAO<T> extends MasterStorage<T> {
    private final JdbcTemplate jdbcTemplate;

    public abstract T make(ResultSet rs, int rowNum) throws SQLException;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public boolean isExist(Long id) {
        if (get(id) == null) {
            String error = String.format("Not found - id:%d not exist", id);
            throw new NotFoundException(error);
        }
        return true;
    }
}
