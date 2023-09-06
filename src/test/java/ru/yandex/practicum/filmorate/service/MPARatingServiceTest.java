package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.controller.ErrorController;
import ru.yandex.practicum.filmorate.dao.MPARatingDAO;
import ru.yandex.practicum.filmorate.dao.rowMapper.MPARatingRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(value = {"/create-mpa-after.sql"}, executionPhase = AFTER_TEST_METHOD)
class MPARatingServiceTest {
    final List<MPARating> mpaRatings = List.of(
            new MPARating(1L, "G", "Нет возрастных ограничений"),
            new MPARating(2L, "PG", "Рекомендуется присутствие родителей"),
            new MPARating(3L, "PG-13", "Детям до 13 лет просмотр не желателен"),
            new MPARating(4L, "R", "Лицам до 17 лет обязательно присутствие взрослого"),
            new MPARating(5L, "NC-17", "Лицам до 18 лет просмотр запрещен")
    );
    private final DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
            .addScript("classpath:/schema.sql")
            .addScript("classpath:/create-mpa-before.sql")
            .build();
    private final MPARatingService mpaRatingService = new MPARatingService(
            new MPARatingDAO(
                    new JdbcTemplate(dataSource),
                    new MPARatingRowMapper()
            )
    );
    private final ErrorController errorController = new ErrorController();

    @Test
    void get() {
        assertEquals(mpaRatings.get(3), mpaRatingService.get(4L));
    }

    @Test
    void getWrongId() {
        try {
            mpaRatingService.get(9999L);
        } catch (NotFoundException e) {
            assertEquals(404, errorController.handleNotFoundException(e).getStatusCode().value());
        }
    }

    @Test
    void getAll() {
        assertEquals(mpaRatings, mpaRatingService.getAll());
    }
}