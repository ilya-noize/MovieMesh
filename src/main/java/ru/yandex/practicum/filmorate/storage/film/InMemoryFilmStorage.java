package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryFilmStorage implements FilmStorage {
    private Long generateId = 1L;
    private final Map<Long, Film> films;

    @Override
    public Film create(Film film) {
        film.setId(generateId++);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (isExist(film.getId())) {
            films.replace(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film get(Long id) {
        Film film = null;
        if (isExist(id)) {
            film = films.get(id);
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean isExist(Long id) {
        if (!films.containsKey(id)) {
            String error = String.format("Фильм не найден: id:%d отсутствует", id);
            log.error(error);
            throw new NotFoundException(error);
        }
        return true;
    }
}
