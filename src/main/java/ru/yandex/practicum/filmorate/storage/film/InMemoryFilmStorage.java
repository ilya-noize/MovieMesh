package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    Integer generateId;
    private final Map<Integer, Film> films;
    private final Map<Integer, Set<Integer>> likes;

    @Autowired
    public InMemoryFilmStorage() {
        this.generateId = 1;
        this.films = new HashMap<>();
        this.likes = new HashMap<>();
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
    public void setLikes(Integer filmId, Set<Integer> userLikes) {
        likes.replace(filmId, userLikes);
    }

    @Override
    public Set<Integer> getLikes(Integer filmId) {
        return likes.get(filmId);
    }
}
