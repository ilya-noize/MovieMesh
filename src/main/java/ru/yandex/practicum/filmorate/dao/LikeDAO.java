package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@Primary
public final class LikeDAO extends MasterStorageDAO<Like> {
    @Autowired
    public LikeDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Like create(Like like) {
        String sql = "INSERT INTO films_like (film_id, user_id) VALUES (?, ?)";
        getJdbcTemplate().update(sql, like.getFilmId(), like.getUserId());
        return like;
    }

    @Override
    public Like update(Like like) {
        String sql = "UPDATE films_like SET user_id = ? WHERE film_id = ?;";
        getJdbcTemplate().update(sql, like.getUserId(), like.getFilmId());
        return like;
    }

    @Override
    public Like get(Long id) {
        return null;
    }

    @Override
    public void delete(Long... id) {
        String sql = "DELETE FROM films_like WHERE film_id = ? AND user_id = ?;";
        getJdbcTemplate().update(sql, id[0], id[1]);
    }

    @Override
    public List<Like> getAll() {
        String sql = "SELECT * FROM films_like";
        return getJdbcTemplate().query(sql, this::make);
    }

    @Override
    public Like make(ResultSet rs, int rowNum) throws SQLException {
        return new Like(rs.getLong("film_id"), rs.getLong("user_id"));
    }
}
