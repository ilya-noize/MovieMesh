package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@Primary
public final class MPARatingDAO extends MasterStorageDAO<MPARating> {
    @Autowired
    public MPARatingDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public MPARating create(MPARating mpaRating) {
        getJdbcTemplate().update("INSERT INTO mpa_rating (rating, description) VALUES (?, ?)",
                mpaRating.getName(), mpaRating.getDescription());
        return mpaRating;
    }

    @Override
    public MPARating update(MPARating mpaRating) {
        getJdbcTemplate().update("UPDATE mpa_rating SET rating = ?, description = ? WHERE id = ?",
                mpaRating.getName(), mpaRating.getDescription(), mpaRating.getId());
        return mpaRating;
    }

    @Override
    public MPARating get(Long id) {
        String error = String.format("MPARating not found - id:%d not exist", id);
        return getJdbcTemplate().query("SELECT * FROM mpa_rating WHERE id = ?",
                        this::make, id)
                .stream().findFirst()
                .orElseThrow(new NotFoundException(error));
    }

    @Override
    public void delete(Long... id) {
        String sql = "DELETE FROM  WHERE id = ?;";
        getJdbcTemplate().update(sql, id[0]);
    }

    @Override
    public List<MPARating> getAll() {
        return getJdbcTemplate().query("SELECT * FROM mpa_rating", this::make);
    }

    @Override
    public MPARating make(ResultSet rs, int rowNum) throws SQLException {
        return new MPARating(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"));
    }
}
