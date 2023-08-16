package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.rowMapper.MPARatingRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Primary
public final class FilmDAO extends MasterStorageDAO<Film> {
    @Autowired
    public FilmDAO(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Film create(Film film) {
        film.setId(increment());
        String sql = "INSERT INTO films"
                + " (name, description, duration, releaseDate, mpa_rating_id)"
                + " VALUES (?, ?, ?, ?, ?)";
        getJdbcTemplate().update(sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        isExist(film.getId());
        updateFilm(film);
        updateFilmGenres(film);
        return film;
    }

    @Override
    public Film get(Long id) {
        String error = String.format("Film not found - id:%d not exist", id);
        String sql = "SELECT * FROM films F WHERE F.id = ?";
        return getJdbcTemplate().queryForStream(sql, this::make, id)
                .findFirst()
                .orElseThrow(new NotFoundException(error));
    }

    @Override
    public void delete(Long... id) {
        String sql = "DELETE FROM films F WHERE id = ?;";
        getJdbcTemplate().update(sql, id[0]);
    }

    @Override
    public List<Film> getAll() {
        String sql = "SELECT * FROM films F;";
        return getJdbcTemplate().query(sql, this::make);
    }

    @Override
    public Film make(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("id");
        String name = rs.getString("Name");
        LocalDate releaseDate = rs.getDate("ReleaseDate").toLocalDate();
        String description = rs.getString("Description");
        Integer duration = rs.getInt("Duration");

        MPARating mpa = getMPA(filmId);
//        Set<GenresFilm> genres = getGenresByFilm(filmId);
        Set<Long> genres = getGenresByFilm(filmId);
        Set<Long> likes = getUserLikes(filmId);
        Integer rate = likes.size();//rs.getInt("Rate");

        return new Film(filmId, name, releaseDate, description, duration, rate, mpa, genres, likes);
    }

    private MPARating getMPA(Long filmId) {
        String sql = "SELECT MPA.* FROM MPA_RATING MPA"
                + " RIGHT JOIN FILMS F ON F.MPA_RATING_ID = MPA.ID"
                + " WHERE F.ID = ?;";
        return getJdbcTemplate().queryForObject(sql, new MPARatingRowMapper(), filmId);
    }

    //    private Set<GenresFilm> getGenresByFilm(Long id) {
    private Set<Long> getGenresByFilm(Long id) {
        String sql = "SELECT genre_id FROM genres_film WHERE film_id = ?";
        return new HashSet<>(getJdbcTemplate().query(
                sql,
                //new GenresFilmRowMapper(),
                (rs, rowNum) -> rs.getLong("genre_id"),
                id));
    }

    private Set<Long> getUserLikes(Long id) {
        String sql = "SELECT user_id FROM films_like WHERE film_id = ?";
        return new HashSet<>(getJdbcTemplate().query(
                sql,
                (rs, rowNum) -> rs.getLong("user_id"),
                id));
    }

    private void updateFilm(Film film) {
        String sql = "UPDATE films SET"
                + " name = ?, description = ?, duration = ?, releaseDate = ?, mpa_rating_id=?"
                + " WHERE id = ?;";
        getJdbcTemplate().update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId()
        );
    }

    private void updateFilmGenres(Film film) {
        String sqlGenre = "UPDATE genres_film SET"
                + " genre_id = ? WHERE film_id = ?;";
        film.getGenres().forEach(
                genre_id -> getJdbcTemplate().update(
                        sqlGenre, genre_id, film.getId()
                )
        );
    }
}
