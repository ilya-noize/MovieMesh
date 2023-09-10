package ru.yandex.practicum.filmorate.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.controller.ErrorController;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.dao.rowMapper.FilmGenresRowMapper;
import ru.yandex.practicum.filmorate.dao.rowMapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.dao.rowMapper.MPARatingRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@EnableAutoConfiguration(exclude = {BatchAutoConfiguration.class})
@Sql(value = {
        "/sql/films/create-films-after.sql",
        "/sql/users/create-users-after.sql",
        "/sql/mpa/create-mpa-after.sql",
        "/sql/genres/create-genres-after.sql",
}, executionPhase = AFTER_TEST_METHOD)
public class FilmServiceTest {
    private final ErrorController errorController = new ErrorController();
    private final DataSource dataSource = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:/sql/schema-test.sql")
            .addScript("classpath:/sql/genres/create-genres-before.sql")
            .addScript("classpath:/sql/mpa/create-mpa-before.sql")
            .addScript("classpath:/sql/users/create-users-before.sql")
            .addScript("classpath:/sql/films/create-films-before.sql")
            .addScript("classpath:/sql/films/genres/create-film-genres-before.sql")
            .addScript("classpath:/sql/films/likes/create-films-like-before.sql")
            .build();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    private final FilmGenresDAO filmGenresDAO = new FilmGenresDAOImpl(
            jdbcTemplate, new FilmGenresRowMapper());
    private final Showable<MPARating> mpaRatingDAO = new MPARatingDAO(
            jdbcTemplate, new MPARatingRowMapper());
    private final FilmDAO filmDAO = new FilmDAOImpl(
            jdbcTemplate, new FilmRowMapper());
    private final FilmService filmService = new FilmService(
            filmDAO, filmGenresDAO, mpaRatingDAO);
    private final Film film = new Film(
            Long.MAX_VALUE,
            "StarWars:Episode X",
            "A long time ago in a galaxy far, far away",
            LocalDate.now(),
            120,
            new MPARating(2L, null, null),
            new ArrayList<>(List.of(
                    new Genre(6L, null),
                    new Genre(5L, null))
            )
    );

    @Test
    public void create() {
        try {
            filmService.create(film);
        } catch (Throwable e) {
            assertEquals(500, errorController.handleMethodArgumentNotValidException(e).getStatusCode().value());
        }
    }

    @Test
    public void createEmptyName() {
        final Film film = new Film(
                Long.MAX_VALUE,
                "",
                "A long time ago in a galaxy far, far away",
                LocalDate.now(),
                120,
                new MPARating(2L, null, null),
                new ArrayList<>(List.of(
                        new Genre(6L, null),
                        new Genre(5L, null))
                )
        );
        try {
            filmService.create(film);
        } catch (ValidException e) {
            assertEquals(400, errorController.handleValidException(e).getStatusCode().value());
        }
    }

    @Test
    public void createNullName() {
        final Film film = new Film(
                Long.MAX_VALUE,
                null,
                "A long time ago in a galaxy far, far away",
                LocalDate.now(),
                120,
                new MPARating(2L, null, null),
                new ArrayList<>(List.of(
                        new Genre(6L, null),
                        new Genre(5L, null))
                )
        );
        try {
            filmService.create(film);
        } catch (ValidException e) {
            assertEquals(400, errorController.handleValidException(e).getStatusCode().value());
        }
    }

    @Test
    public void updateFailId() {
        try {
            filmService.update(film);
        } catch (NotFoundException e) {
            assertEquals(404, errorController.handleNotFoundException(e).getStatusCode().value());
        }

    }

    @Test
    public void updateFailRename() {
        final Film film = new Film(
                1L,
                null,
                "A long time ago in a galaxy far, far away",
                LocalDate.now(),
                120,
                new MPARating(2L, null, null),
                new ArrayList<>(List.of(
                        new Genre(6L, null),
                        new Genre(5L, null))
                )
        );
        try {
            filmService.update(film);
        } catch (ValidException e) {
            assertEquals(400, errorController.handleValidException(e).getStatusCode().value());
        }
    }

    @Test
    public void get() {
        final Film getFilm = filmService.get(1L);
        assertEquals("StarWars:Episode I", getFilm.getName());
    }

    @Test
    public void getAll() {
        final List<Film> films = filmService.getAll();
        assertEquals(9, films.size());
    }

    @Test
    public void getPopular() {
        final Long count = 4L;
        final List<Long> ids = filmService.getPopular(count)
                .stream()
                .map(Film::getId)
                .collect(Collectors.toList());
        final List<Long> assertIds = List.of(9L, 8L, 2L, 1L);
        assertFalse(ids.containsAll(assertIds));
    }
}