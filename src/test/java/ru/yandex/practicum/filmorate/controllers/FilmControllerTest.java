package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class FilmControllerTest {
    private static final LocalDate RIGHT_RELEASE = LocalDate.of(1895,12,28);
    private static final LocalDate WRONG_RELEASE = LocalDate.of(1895,12,27);
    private static final int RIGHT_DURATION = 100;
    private static final int WRONG_DURATION = -1;

    FilmController controller = new FilmController();

    @DisplayName(value = "Создание фильма")
    @Test
    void createFilm() {
        Film film = controller.createFilm(getFilm());
        assertNotNull(film.getId());
    }

    private Film getFilm() {
        return new Film("name","description",RIGHT_RELEASE,RIGHT_DURATION);
    }

    @DisplayName(value = "Создание фильма - Ошибка:null-название")
    @Test
    void createFilmFailNullName() {
        try {
            controller.createFilm(getFilmNullName());
        } catch (Exception e) {
            assertEquals("Название фильма не может быть null.", e.getMessage());
        }
    }

    private Film getFilmNullName() {
        return new Film(null, "description", RIGHT_RELEASE, RIGHT_DURATION);
    }

    @DisplayName(value = "Создание фильма - Ошибка:пустое название")
    @Test
    void createFilmFailBlankName() {
        try {
            controller.createFilm(getFilmBlankName());
        } catch (Exception e) {
            assertEquals("Название фильма не может быть пустым.", e.getMessage());
        }
    }

    private Film getFilmBlankName() {
        return new Film("", "description", RIGHT_RELEASE, RIGHT_DURATION);
    }

    @DisplayName(value = "Создание фильма - Ошибка:Большое описание")
    @Test
    void createFilmFailLongDescription() {
        try {
            controller.createFilm(getFilmLongDescription());
        } catch (Exception e) {
            assertEquals("Длина описания не более 200 символов.", e.getMessage());
        }
    }

    private Film getFilmLongDescription() {
        String description = "You will build a simple Spring application and test it with JUnit. " +
                "You probably already know how to write and run unit tests of the individual classes " +
                "in your application, so, for this guide, we will concentrate on using Spring Test " +
                "and Spring Boot features to test the interactions between Spring and your code. " +
                "You will start with a simple test that the application context loads successfully " +
                "and continue on to test only the web layer by using Spring’s MockMvc.";
        return new Film("name", description, RIGHT_RELEASE, RIGHT_DURATION);
    }

    @DisplayName(value = "Создание фильма - Ошибка:Неверная дата релиза")
    @Test
    void createFilmFailWrongRelease() {
        try {
            controller.createFilm(getFilmWrongRelease());
        } catch (Exception e) {
            assertEquals("Дата релиза не раньше 28 DEC 1895 и не позже сегодня", e.getMessage());
        }
    }

    private Film getFilmWrongRelease() {
        return new Film("name", "description", WRONG_RELEASE, RIGHT_DURATION);
    }

    @DisplayName(value = "Создание фильма - Ошибка: Отрицательная продолжительность фильма")
    @Test
    void createFilmFailWrongDuration() {
        try {
            controller.createFilm(getFilmWrongDuration());
        } catch (Exception e) {
            assertEquals("Продолжительность фильма - положительное натуральное число.", e.getMessage());
        }
    }

    private Film getFilmWrongDuration() {
        return new Film("name", "description", RIGHT_RELEASE, WRONG_DURATION);
    }

    @DisplayName(value = "Обновление фильма")
    @Test
    void updateFilm() {
        Film film = controller.createFilm(getFilm());
        film.setDescription("new description");
        assertEquals(1, controller.updateFilm(film).getId());
    }

    @DisplayName(value = "Обновление фильма - Ошибка: нет такого фильма")
    @Test
    void updateNotExistFilm() {
        try {
            controller.createFilm(getNotExistFilm());
        } catch (Exception e) {
            assertEquals("Нет такого фильма.", e.getMessage());
        }
    }

    private static Film getNotExistFilm() {
        return new Film(9999, "name", "description", RIGHT_RELEASE, RIGHT_DURATION);
    }

    @DisplayName(value = "Получить список фильмов")
    @Test
    void getFilms() {
        controller.createFilm(getFilm());
        int countFilms = controller.getFilms().size();
        assertEquals(1, countFilms);
    }
}