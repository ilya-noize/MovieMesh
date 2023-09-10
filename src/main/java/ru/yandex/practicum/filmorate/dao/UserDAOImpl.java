package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public final class UserDAOImpl implements UserDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper;

    public User create(final User user) {
        String sql = "INSERT INTO users (login, name, email, birthday)"
                + " VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        PreparedStatementCreator psc = con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        };
        jdbcTemplate.update(psc, keyHolder);
        Long userId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new User(
                userId,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
    }

    public User update(final User user) {
        String sql = "UPDATE users SET login = ?, name = ?, email = ?, birthday = ? WHERE id = ?;";
        jdbcTemplate.update(
                sql,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public User get(final Long id) {
        String error = String.format("User not found - id:%d not exist", id);
        String sql = "SELECT * FROM users WHERE id = ?";
        return jdbcTemplate.query(sql, userRowMapper, id)
                .stream().findFirst()
                .orElseThrow(new NotFoundException(error));
    }

    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    @Override
    public void createFriend(Long userRequest, Long userFriend) {
        String sql = "MERGE INTO friends KEY(user_id_request, user_id_friend) VALUES (?, ?)";
        jdbcTemplate.update(sql, userRequest, userFriend);
    }

    @Override
    public void deleteFriend(Long userRequest, Long userFriend) {
        String sql = "DELETE FROM friends WHERE user_id_request = ? AND user_id_friend = ?;";
        jdbcTemplate.update(sql, userRequest, userFriend);
    }

    @Override
    public List<User> getFriends(Long id) {
        String sql = "SELECT U.* FROM FRIENDS FR"
                + " JOIN USERS U ON FR.USER_ID_FRIEND = U.ID"
                + " WHERE FR.USER_ID_REQUEST = ?";
        return jdbcTemplate.query(sql, userRowMapper, id);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        String sql = "SELECT U.* FROM FRIENDS F1"
                + " JOIN FRIENDS F2"
                + "  ON F2.USER_ID_REQUEST  = ?"
                + "  AND F2.USER_ID_FRIEND  = F1.USER_ID_FRIEND"
                + " JOIN USERS U"
                + "  ON U.ID = F1.USER_ID_FRIEND"
                + " WHERE F1.USER_ID_REQUEST = ?";
        return jdbcTemplate.query(sql, userRowMapper, id, otherId);
    }
}
