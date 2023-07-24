package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
public class FilmStorage extends MainStorage<Film> {
    protected FilmStorage(Map<Long, Film> storage) {
        super(storage);
    }

    @Override
    public Film create(Film film) {
        film.setId(increment());
        film.setLikes(new HashSet<>());
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (isExist(film.getId())) {
            storage.replace(film.getId(), film);
        }
        return film;
    }

    @Override
    public Film get(Long id) {
        Film film = null;
        if (isExist(id)) {
            film = storage.get(id);
        }
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean isExist(Long id) {
        if (!storage.containsKey(id)) {
            String error = String.format("Фильм не найден: id:%d отсутствует", id);
            throw new NotFoundException(error);
        }
        return true;
    }
}
