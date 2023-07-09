package ru.yandex.practicum.filmorate.controllers;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController extends Controller {
    private final Map<Integer, Film> films;

    public FilmController() {
        films = new HashMap<>();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос POST к эндпоинту: /films");

        film.setId(generateId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм({})", film);
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос PUT к эндпоинту: /films");

        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.info("Изменён фильм({})", film);
            return film;
        }
        throw new NotFoundException("Нет такого фильма.");
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("Получен запрос GET к эндпоинту: /films");

        return new ArrayList<>(films.values());
    }
}