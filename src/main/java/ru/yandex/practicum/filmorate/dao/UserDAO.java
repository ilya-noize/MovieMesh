package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Primary
public final class UserDAO extends MasterStorageDAO<User> {
    @Autowired
    public UserDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public User create(final User user) {
        user.setId(increment());
        String sql = "INSERT INTO users (id, login, name, email, birthday)"
                + " VALUES (?, ?, ?, ?, ?)";
        getJdbcTemplate().update(
                sql,
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday()
        );
        return user;
    }

    @Override
    public User update(final User user) {
        isExist(user.getId());
        String sql = "UPDATE users SET login = ?, name = ?, email = ?, birthday = ? WHERE id = ?;";
        getJdbcTemplate().update(
                sql,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public User get(final Long id) {
        String error = String.format("User not found - id:%d not exist", id);
        String sql = "SELECT * FROM users WHERE id = ?";
        return getJdbcTemplate().query(sql, this::make, id)
                .stream().findFirst()
                .orElseThrow(new NotFoundException(error));
    }

    @Override
    public void delete(Long[] id) {
        String sql = "DELETE FROM users WHERE id = ?;";
        getJdbcTemplate().update(sql, id[0]);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM users";
        return getJdbcTemplate().query(sql, this::make);
    }

    @Override
    public User make(ResultSet rs, int rowNum) throws SQLException {
        Long userId = rs.getLong("id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        Set<Long> friends = getAllUserFriends(userId);
        Set<Long> likes = getLikesFilmId(userId);

        return new User(userId, email, login, name, birthday, friends, likes);
    }

    private Set<Long> getAllUserFriends(Long id) {
        String sql = "SELECT user_id_friend FROM friends WHERE user_id_request = ?";
        return new HashSet<>(getJdbcTemplate().query(
                sql,
                (rs, rowNum) -> rs.getLong("user_id_friend"),
                id));
    }

    private Set<Long> getLikesFilmId(Long id) {
        String sql = "SELECT film_id FROM films_like WHERE user_id = ?";
        return new HashSet<>(getJdbcTemplate().query(
                sql,
                (rs, rowNum) -> rs.getLong("film_id"),
                id)
        );
    }
}
