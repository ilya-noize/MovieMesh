package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.WrongFilmIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.FilmServiceImpl;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
class FilmControllerTest {
    final LocalDate rightRelease = LocalDate.of(1895, 12, 28);
    final int rightDuration = 1;

    UserStorage userStorage = new InMemoryUserStorage();
    FilmStorage filmStorage = new InMemoryFilmStorage();
    FilmService service = new FilmServiceImpl(filmStorage, userStorage);
    FilmController controller = new FilmController(service);

    @DisplayName(value = "Создание фильма")
    @Test
    void createFilm() {
        Film film = controller.create(getFilm());
        assertNotNull(film.getId());
    }

    private Film getFilm() {
        return new Film("name", "description", rightRelease, rightDuration);
    }

    @DisplayName(value = "Создание фильма - Ошибка:null-название")
    @Test
    void createFilmFailNullName() {
        try {
            controller.create(getFilmNullName());
        } catch (ValidationException e) {
            assertEquals("Название фильма не может быть null.", e.getMessage());
        }
    }

    private Film getFilmNullName() {
        return new Film(null, "description", rightRelease, rightDuration);
    }

    @DisplayName(value = "Создание фильма - Ошибка:пустое название")
    @Test
    void createFilmFailBlankName() {
        try {
            controller.create(getFilmBlankName());
        } catch (ValidationException e) {
            assertEquals("Название фильма не может быть пустым.", e.getMessage());
        }
    }

    private Film getFilmBlankName() {
        return new Film("", "description", rightRelease, rightDuration);
    }

    @DisplayName(value = "Создание фильма - Ошибка:Большое описание")
    @Test
    void createFilmFailLongDescription() {
        try {
            controller.create(getFilmLongDescription());
        } catch (ValidationException e) {
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
        return new Film("name", description, rightRelease, rightDuration);
    }

    @DisplayName(value = "Создание фильма - Ошибка:Неверная дата релиза")
    @Test
    void createFilmFailWrongRelease() {
        try {
            controller.create(getFilmWrongRelease());
        } catch (ValidationException e) {
            assertEquals("Дата релиза не раньше 28 DEC 1895 и не позже сегодня", e.getMessage());
        }
    }

    private Film getFilmWrongRelease() {
        LocalDate wrongRelease = LocalDate.of(1895, 12, 27);
        return new Film("name", "description", wrongRelease, rightDuration);
    }

    @DisplayName(value = "Создание фильма - Ошибка: Отрицательная продолжительность фильма")
    @Test
    void createFilmFailWrongDuration() {
        try {
            controller.create(getFilmWrongDuration());
        } catch (ValidationException e) {
            assertEquals("Продолжительность фильма - положительное натуральное число.", e.getMessage());
        }
    }

    private Film getFilmWrongDuration() {
        int wrongDuration = -1;
        return new Film("name", "description", rightRelease, wrongDuration);
    }

    @DisplayName(value = "Обновление фильма")
    @Test
    void updateFilm() {
        Film film = controller.create(getFilm());
        film.setDescription("new description");
        assertEquals(1, controller.update(film).getId());
    }

    @DisplayName(value = "Обновление фильма - Ошибка: нет такого фильма")
    @Test
    void updateNotExistFilm() {
        try {
            controller.create(getNotExistFilm());//todo wtf
        } catch (NotFoundException e) {
            assertEquals("Нет такого фильма.", e.getMessage());
        }
    }

    private Film getNotExistFilm() {
        Film film = getFilm();
        film.setId(9999);
        return film;
    }

    @DisplayName(value = "Получить список фильмов")
    @Test
    void getFilms() {
        controller.create(getFilm());
        int countFilms = controller.getAll().size();
        assertEquals(1, countFilms);
    }


    @DisplayName(value = "Получить популярные фильмы")
    @Test
    void getPopular() {
        controller.getPopular("1");
    }

    @DisplayName(value = "Добавить лайк")
    @Test
    void addLike() {
        try {
            controller.addLike("", "");
        } catch (WrongFilmIdException e){
            assertEquals("Неверный уин фильма: -2147483648", e.getMessage());
        }
    }

    @DisplayName(value = "Удалить лайк")
    @Test
    void deleteLike() {
        try {
            controller.deleteLike("","");
        } catch (WrongFilmIdException e){
            assertEquals("Неверный уин фильма: -2147483648", e.getMessage());
        }
    }
}