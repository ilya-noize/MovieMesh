package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
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
        ifExist(film.getId());
        films.replace(film.getId(), film);
        return film;
    }

    @Override
    public Film ifExist(Integer filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Нет такого фильма.");
        } else {
            return films.get(filmId);
        }
    }

    /**
     * get Film by id
     *
     * @param id id-film
     * @return Film
     */
    @Override
    public Film get(Integer id) {
        return films.get(id);
    }

//todo сделать поиск
//    public Optional<Film> getByName(String name) {
//        return getAll().stream()
//                .filter(film -> film.getName().contains(name))
//                .findFirst();
//    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public void like(Integer filmId, Integer userId) {
        Film film = ifExist(filmId);
        if (film.getLikes().add(userId)) {
            films.replace(filmId, film);
        }

    }

    @Override
    public void unLike(Integer filmId, Integer userId) {
        Film film = ifExist(filmId);
        if (film.getLikes().remove(userId)) {
            films.replace(filmId, film);
        }
    }

    @Override
    public List<Film> getPopular(Integer count) {
        if (count < 0) {
            throw new IllegalArgumentException();
        }
        return films.values().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
