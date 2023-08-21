package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.PreparedStatement;
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
        String sql = "INSERT INTO mpa_rating (rating, description)"
                + " VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, mpaRating.getName());
            ps.setString(2, mpaRating.getDescription());
            return ps;
        };
        getJdbcTemplate().update(psc, keyHolder);
        Long mpaId = keyHolder.getKey().longValue();
        mpaRating.setId(mpaId);
        return mpaRating;
    }

    @Override
    public MPARating update(MPARating mpaRating) {
        String sql = "UPDATE mpa_rating"
                + " SET rating = ?, description = ? WHERE id = ?";
        getJdbcTemplate().update(
                sql,
                mpaRating.getName(),
                mpaRating.getDescription(),
                mpaRating.getId());
        return mpaRating;
    }

    @Override
    public MPARating get(Long id) {
        String sql = "SELECT * FROM mpa_rating WHERE id = ?";
        String error = String.format("MPARating not found - id:%d not exist", id);
        return getJdbcTemplate().query(sql, this::make, id)
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
        String sql = "SELECT * FROM mpa_rating";
        return getJdbcTemplate().query(sql, this::make);
    }

    @Override
    public MPARating make(ResultSet rs, int rowNum) throws SQLException {
        return new MPARating(rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"));
    }
}
