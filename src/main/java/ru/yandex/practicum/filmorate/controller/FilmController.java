package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends Controller {
    FilmService filmService;

    @Autowired(required = false)
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST к endpoint-у: /films");
        film = filmService.create(film);
        log.info("Добавлен фильм({})", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT к endpoint-у: /films");
        return filmService.update(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable String filmId, String userId) { // todo test
        log.info("Получен запрос PUT к endpoint-у: /films" + filmId + "/like/" + userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable String filmId, String userId) { // todo test
        log.info("Получен запрос PUT к endpoint-у: /films/" + filmId + "/like/" + userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping()
    public List<Film> getAll() {
        log.info("Получен запрос GET к endpoint-у: /films");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable String id) {
        log.info("Получен запрос GET к endpoint-у: /films");
        return filmService.get(id);
    }

    @GetMapping("/films/popular?count={count}")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10", required = false) String count) {
        log.info("Получен запрос GET к endpoint-у: /films/popular?count=" + count);
        return filmService.getPopular(count);
    }


    @ExceptionHandler
    public Map<String, String> handleNegativeCount(final IllegalArgumentException e) {
        return Map.of("error", "Передан отрицательный параметр count.");
    }
}