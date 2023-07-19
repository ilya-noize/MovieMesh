package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

/**
 * POST /films — Добавляет фильма.
 * PUT /films — Обновляет фильма.
 * PUT /films/{id}/like/{userId} — Пользователь ставит лайк фильму.
 * DELETE /films/{id}/like/{userId} — Пользователь удаляет лайк.
 * GET /films — Возвращает список всех фильмов
 * GET /films/popular?count={count} — Возвращает список из первых count* фильмов по количеству лайков.
 * * Если значение параметра count не задано, вернуть первые 10.
 */

@RestController
@RequestMapping("/films")
public class FilmController extends Controller {
    FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * Добавляет фильма.
     *
     * @param film фильм
     * @return фильм
     */
    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST к endpoint-у: /films");
        film = filmService.create(film);
        log.info("Добавлен фильм({})", film);
        return film;
    }

    /**
     * Обновляет фильма.
     *
     * @param film фильм
     * @return фильм
     */
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT к endpoint-у: /films");
        return filmService.update(film);
    }

    /**
     * Пользователь ставит лайк фильму.
     *
     * @param filmId уин фильма
     * @param userId уин пользователя
     */
    @PutMapping("/films/{filmId}/like/{userId}")
    public void like(@PathVariable Integer filmId, Integer userId) { // todo test
        log.info("Получен запрос PUT к endpoint-у: /films" + filmId + "/like/" + userId);
        filmService.like(filmId, userId);
    }

    /**
     * Пользователь удаляет лайк фильму.
     *
     * @param filmId уин фильма
     * @param userId уин пользователя
     */
    @DeleteMapping("/films/{filmId}/like/{userId}")
    public void unLike(@PathVariable Integer filmId, Integer userId) { // todo test
        log.info("Получен запрос PUT к endpoint-у: /films/" + filmId + "/like/" + userId);
        filmService.unLike(filmId, userId);
    }

    /**
     * Возвращает список всех фильмов
     *
     * @return список
     */
    @GetMapping("/films")
    public List<Film> getAll() {
        log.info("Получен запрос GET к endpoint-у: /films");
        return filmService.getAll();
    }

    /**
     * Возвращает фильм
     *
     * @return 1 pcs
     */
    @GetMapping("/films/{id}")
    public Film get(@PathVariable Integer id) { // todo test
        log.info("Получен запрос GET к endpoint-у: /films");
        return filmService.get(id);
    }

    /**
     * Возвращает список из первых count* фильмов по количеству лайков.
     *
     * @param count def 10
     * @return список
     */
    @GetMapping("/films/popular?count={count}")
    public List<Film> getPopular( // todo test
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        log.info("Получен запрос GET к endpoint-у: /films/popular?count=" + count);
        return filmService.getPopular(count);
    }
}