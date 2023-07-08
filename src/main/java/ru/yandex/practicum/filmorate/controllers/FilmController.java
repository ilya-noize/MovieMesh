package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.FilmorateApplication.log;


@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films;
    private Integer generateId = 1;

    public FilmController() {
        films = new HashMap<>();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST к эндпоинту: /films");

        film.setId(generateId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм({})", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT к эндпоинту: /films");

        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.info("Изменён фильм({})", film);
            return film;
        }
        throw new RuntimeException("Нет такого фильма.");
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен запрос GET к эндпоинту: /films");

        return new ArrayList<>(films.values());
    }
}