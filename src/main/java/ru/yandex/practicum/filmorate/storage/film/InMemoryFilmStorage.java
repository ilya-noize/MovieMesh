package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    Integer generateId;
    private final Map<Integer, Film> films;

    @Autowired
    public InMemoryFilmStorage() {
        this.generateId = 1;
        this.films = new HashMap<>();
    }

    @Override
    public Film create(Film film) {
        film.setId(generateId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.replace(film.getId(), film);
        return film;
    }

    @Override
    public Film get(Integer id) {
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        Film film = get(filmId);
        if (film.getLikes().add(userId)) {
            films.replace(filmId, film);
        }

    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        Film film = get(filmId);
        if (film.getLikes().remove(userId)) {
            films.replace(filmId, film);
        }
    }

    @Override
    public List<Film> getPopular(Integer count) {
        return getAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
