package ru.yandex.practicum.filmorate.dao.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MPARatingRowMapper implements RowMapper<MPARating> {
    @Override
    public MPARating mapRow(ResultSet rs, int rowNum) throws SQLException {
        MPARating mpaRating = new MPARating();
        mpaRating.setId(rs.getLong("id"));
        mpaRating.setName(rs.getString("name"));
        mpaRating.setDescription(rs.getString("description"));

        return mpaRating;
    }
}
