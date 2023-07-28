package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

import java.util.List;

@Slf4j
@Service
public abstract class FilmService {
    private final MasterStorage<Film> filmStorage;

    protected FilmService(MasterStorage<Film> filmStorage) {
        this.filmStorage = filmStorage;
    }

    public abstract void deleteLike(Long supposedId, Long supposedUserId);

    public abstract void addLike(Long supposedId, Long supposedUserId);

    public abstract List<Film> getPopular(Long supposedCount);

    /**
     * Создаёт фильм
     *
     * @param film фильм
     * @return фильм
     */
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    /**
     * Обновляет фильм
     *
     * @param film фильм
     * @return фильм
     */
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    /**
     * Возвращает фильм
     *
     * @param supposedId уин фильма
     * @return фильм
     */
    public Film get(Long supposedId) {
        return filmStorage.get(supposedId);
    }

    /**
     * Возвращает список фильмов
     *
     * @return список
     */
    public List<Film> getAll() {
        return filmStorage.getAll();
    }
}