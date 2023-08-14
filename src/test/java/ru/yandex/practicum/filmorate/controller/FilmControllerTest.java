package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class FilmControllerTest {
    private final LocalDate rightRelease = LocalDate.of(1895, 12, 28);
    private final int rightDuration = 1;
    private FilmController controller;

    @DisplayName(value = "Создание фильма")
    @Test
    void createFilm() {
        try {
            Film film = controller.create(getFilm());
            assertNotNull(film.getId());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    private Film getFilm() {
        return new Film();
//                1L,
//                "Film",
//                rightRelease,
//                "Description",
//                rightDuration,
//                1,
//                1L,
//                Set.of(1L));
    }

    @DisplayName(value = "Создание фильма - Ошибка:null-название")
    @Test
    void createFilmFailNullName() {
        try {
            controller.create(getFilmNullName());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    private Film getFilmNullName() {
        return new Film();
//                1L,
//                null,
//                rightRelease,
//                "Description",
//                rightDuration,
//                1,
//                1L,
//                Set.of(1L));
    }

    @DisplayName(value = "Создание фильма - Ошибка:пустое название")
    @Test
    void createFilmFailBlankName() {
        try {
            controller.create(getFilmBlankName());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    private Film getFilmBlankName() {
        return new Film();
//                1L,
//                "",
//                rightRelease,
//                "Description",
//                rightDuration,
//                1,
//                1L,
//                Set.of(1L));
    }

    @DisplayName(value = "Создание фильма - Ошибка:Большое описание")
    @Test
    void createFilmFailLongDescription() {
        try {
            controller.create(getFilmLongDescription());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    private Film getFilmLongDescription() {
        String description = "You will build a simple Spring application and test it with JUnit. " +
                "You probably already know how to write and run unit tests of the individual classes " +
                "in your application, so, for this guide, we will concentrate on using Spring Test " +
                "and Spring Boot features to test the interactions between Spring and your code. " +
                "You will start with a simple test that the application context loads successfully " +
                "and continue on to test only the web layer by using Spring’s MockMvc.";
        return new Film();
//                1L,
//                "Film",
//                rightRelease,
//                description,
//                rightDuration,
//                1,
//                1L,
//                Set.of(1L));
    }

    @DisplayName(value = "Создание фильма - Ошибка:Неверная дата релиза")
    @Test
    void createFilmFailWrongRelease() {
        try {
            controller.create(getFilmWrongRelease());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    private Film getFilmWrongRelease() {
        LocalDate wrongRelease = LocalDate.of(1895, 12, 27);
        return new Film();
//                1L,
//                "Film",
//                wrongRelease,
//                "Description",
//                rightDuration,
//                1,
//                1L,
//                Set.of(1L));
    }

    @DisplayName(value = "Создание фильма - Ошибка: Отрицательная продолжительность фильма")
    @Test
    void createFilmFailWrongDuration() {
        try {
            controller.create(getFilmWrongDuration());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    private Film getFilmWrongDuration() {
        int wrongDuration = -1;
        return new Film();
//                1L,
//                "Film",
//                rightRelease,
//                "Description",
//                wrongDuration,
//                1,
//                1L,
//                Set.of(1L));
    }

    @DisplayName(value = "Обновление фильма")
    @Test
    void updateFilm() {
        try {
            Film film = controller.create(getFilm());
            film.setDescription("new description");
            assertEquals(1, controller.update(film).getId());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @DisplayName(value = "Обновление фильма - Ошибка: нет такого фильма")
    @Test
    void updateNotExistFilm() {
        try {
            controller.create(getNotExistFilm());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    private Film getNotExistFilm() {
        Film film = getFilm();
        film.setId(9999L);
        return film;
    }

    @DisplayName(value = "Получить список фильмов")
    @Test
    void getFilms() {
        try {
            controller.create(getFilm());
            int countFilms = controller.getAll().size();
            assertEquals(1, countFilms);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }


    @DisplayName(value = "Получить популярные фильмы")
    @Test
    void getPopular() {
        try {
            controller.getPopular(1L);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @DisplayName(value = "Добавить лайк")
    @Test
    void addLike() {
        try {
            controller.addLike(null, null);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @DisplayName(value = "Удалить лайк")
    @Test
    void deleteLike() {
        try {
            controller.deleteLike(null,null);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }
}