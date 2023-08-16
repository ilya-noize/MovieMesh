package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friends;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
@Primary
public final class FriendsDAO extends MasterStorageDAO<Friends> {
    public FriendsDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Friends create(Friends friends) {
        String sql = "INSERT INTO friends (user_id_request, user_id_friend) VALUES (?, ?)";
        getJdbcTemplate().update(sql, friends.getRequestId(), friends.getFriendId());
        return friends;
    }

    @Override
    public Friends update(Friends friends) {
        String sql = "UPDATE friends SET user_id_friend = ? WHERE user_id_request = ?;";
        getJdbcTemplate().update(sql, friends.getFriendId(), friends.getRequestId());
        return friends;
    }

    @Override
    public Friends get(Long id) {
        String sql = "SELECT * FROM friends WHERE user_id_request = ?";
        return getJdbcTemplate().query(sql, this::make, id).get(0);
    }

    @Override
    public void delete(Long... id) {
        String sql = "DELETE FROM friends WHERE user_id_request = ? AND user_id_friend = ?;";
        Long userRequest = id[0];
        Long userFriend = id[1];
        if (isExist(userRequest) && isExist(userFriend)) {
            getJdbcTemplate().update(sql, userRequest, userFriend);
        }
    }

    @Override
    public List<Friends> getAll() {
        String sql = "SELECT * FROM friends";
        return getJdbcTemplate().query(sql, this::make);
    }

    @Override
    public Friends make(ResultSet rs, int rowNum) throws SQLException {
        return new Friends(rs.getLong("user_id_request"), rs.getLong("user_id_friend"));
    }
}
