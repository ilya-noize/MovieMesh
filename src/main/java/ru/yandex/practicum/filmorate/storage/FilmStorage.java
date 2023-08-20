package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class FilmStorage extends MasterStorage<Film> {
    private final Map<Long, Film> storage;

    @Override
    public Film create(Film film) {
        film.setId(increment());
        if (film.getGenres() == null) {
            film.setGenres(List.of());
        }
        Long filmId = film.getId();
        storage.put(film.getId(), film);
        log.info("create film({}): obj({})", filmId, film);
        return film;
    }

    @Override
    public Film update(Film film) {
        Long filmId = film.getId();
        if (isExist(filmId)) {
            storage.replace(filmId, film);
        }
        log.info("update film({}): obj({})", filmId, film);
        return film;
    }

    @Override
    public Film get(Long id) {
        Film film = null;
        if (isExist(id)) {
            film = storage.get(id);
            Long filmId = film.getId();
            log.info("get film({})", filmId);
        }
        return film;
    }

    @Override
    public void delete(Long... id) {
        storage.remove(id[0]);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean isExist(Long id) {
        if (!storage.containsKey(id)) {
            String error = String.format("film not found - id:%d not exist", id);
            throw new NotFoundException(error);
        }
        return true;
    }
}
