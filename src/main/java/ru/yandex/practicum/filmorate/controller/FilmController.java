package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.info("Создание фильма:{}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("Обновление фильма:{}", film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Поставить лайк фильму-id:{} от пользователя-id:{}", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Удалить лайк фильму-id:{} от пользователя-id:{}", filmId, userId);
        filmService.deleteLike(filmId, userId);
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable Long filmId) {
        log.info("Получить фильм с id:{}", filmId);
        return filmService.get(filmId);
    }

    @GetMapping({"/popular?count={count}", "/popular"})
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Long count) {
        return filmService.getPopular(count);
    }
}