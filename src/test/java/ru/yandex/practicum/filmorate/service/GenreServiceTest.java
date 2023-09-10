package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controller.ErrorController;
import ru.yandex.practicum.filmorate.dao.GenreDAO;
import ru.yandex.practicum.filmorate.dao.rowMapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

/**
 * Тестирую работу жанров.
 * Для этого нужно: <br/>
 * добавить Жанры (get, getAll)<br/>
 * добавить МПА рейтинги (для создания фильмов) <br/>
 * добавить фильмы <br/>
 * добавить жанры к фильмам (getFilmGenres)<br/>
 */

@SpringBootTest
@Sql(value = {
        "/sql/films/genres/create-film-genres-after.sql",
        "/sql/films/create-films-after.sql",
        "/sql/mpa/create-mpa-after.sql",
        "/sql/genres/create-genres-after.sql"
}, executionPhase = AFTER_TEST_METHOD)
public class GenreServiceTest {
    final List<Genre> genres = List.of(
            new Genre(1L, "Комедия"),
            new Genre(2L, "Драма"),
            new Genre(3L, "Мультфильм"),
            new Genre(4L, "Триллер"),
            new Genre(5L, "Документальный"),
            new Genre(6L, "Боевик")//,
            //new Genre(7L,  "Фантастика"),
            //new Genre(8L,  "Вестерн"),
            //new Genre(9L,  "Детектив"),
            //new Genre(10L, "Нуар"),
            //new Genre(11L, "Ужасы"),
            //new Genre(12L, "Политика"),
            //new Genre(13L, "Мюзикл"),
            //new Genre(14L, "Мелодрама"),
            //new Genre(15L, "Сказка")
    );
    private final DataSource dataSource = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:/sql/schema-test.sql")
            .addScript("classpath:/sql/genres/create-genres-before.sql")
            .addScript("classpath:/sql/mpa/create-mpa-before.sql")
            .addScript("classpath:/sql/films/create-films-before.sql")
            .addScript("classpath:/sql/films/genres/create-film-genres-before.sql")
            .build();
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
    private final GenreService genreService = new GenreService(
            new GenreDAO(
                    jdbcTemplate,
                    new GenreRowMapper()
            )
    );
    private final ErrorController errorController = new ErrorController();

    @Test
    void get() {
        assertEquals("Комедия", genreService.get(1L).getName());
    }

    @Test
    void getWrongId() {
        try {
            genreService.get(9999L);
        } catch (NotFoundException e) {
            assertEquals(404, errorController.handleNotFoundException(e).getStatusCode().value());
        }
    }

    @Test
    void getAll() {
        assertEquals(genres, genreService.getAll());
    }
}