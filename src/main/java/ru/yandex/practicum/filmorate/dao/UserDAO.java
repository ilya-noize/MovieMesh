package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDAO extends MasterStorage<User> {
//        implements Storage<User> {
    private final JdbcTemplate jdbcTemplate;
    @Override
    public User create(User user) {
        user.setId(increment());
        String sql = "INSERT INTO 'USERS' (id, login, name, email, birthday) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getId(), user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "UPDATE 'USERS' SET login = ?, name = ?, email = ?, birthday = ? WHERE id = ?;";
        jdbcTemplate.update(sql, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User get(Long id) {
        String sql = "SELECT * FROM 'USERS' WHERE 'id' = ?";
        return jdbcTemplate.query(sql, new Object[]{id},
                new BeanPropertyRowMapper<>(User.class))
                .stream().findFirst().orElse(null);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM 'USERS'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public boolean isExist(Long id) {
        return false;
    }
}
