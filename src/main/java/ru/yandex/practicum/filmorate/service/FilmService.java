package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MainStorage;

import java.util.List;

@Slf4j
@Service
public abstract class FilmService extends MainService<Film> {
    protected final MainService<User> userService;

    protected FilmService(MainStorage<Film> storage, MainService<User> userService) {
        super(storage);
        this.userService = userService;
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
    @Override
    public Film create(Film film) {
        return storage.create(film);
    }

    /**
     * Обновляет фильм
     *
     * @param film фильм
     * @return фильм
     */
    @Override
    public Film update(Film film) {
        log.info("Update Film({})", film.getId());
        return storage.update(film);
    }

    /**
     * Возвращает фильм
     *
     * @param supposedId уин фильма
     * @return фильм
     */
    @Override
    public Film get(Long supposedId) {
        Film film = storage.get(supposedId);
        log.info("get Film({})", supposedId);
        return film;
    }

    /**
     * Возвращает список фильмов
     *
     * @return список
     */
    @Override
    public List<Film> getAll() {
        log.info("get all Films");
        return storage.getAll();
    }

    @Override
    public Film valid(Film film) {
        return null;
    }
}