package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.WrongIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserService userService;


    @Autowired
    public FilmService(FilmStorage filmStorage,
                       @Autowired(required = false) UserService userService){
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    /**
     * Создаёт фильм
     * @param film фильм
     * @return фильм
     */
    public Film create(Film film) {
        return filmStorage.create(film);
    }

    /**
     * Обновляет фильм
     * @param film фильм
     * @return фильм
     */
    public Film update(Film film) {
        return filmStorage.update(film);
    }

    /**
     * Возвращает фильм
     * @param supposedId уин фильма
     * @return фильм
     */
    public Film get(String supposedId) {
        return getFilmFromData(supposedId);
    }

    /**
     * Возвращает список фильмов
     * @return список
     */
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    /**
     * Добавляет лайк от пользователя
     * @param supposedId уин фильма
     * @param supposedUserId уин пользователя
     */
    public void addLike(String supposedId, String supposedUserId) {
        Film film = get(supposedId);
        User user = userService.get(supposedUserId);
        filmStorage.addLike(film.getId(), user.getId());
    }

    /**
     * Удаляет лайк пользователя
     * @param supposedId уин фильма
     * @param supposedUserId уин пользователя
     */
    public void deleteLike(String supposedId, String supposedUserId) {
        Film film = get(supposedId);
        User user = userService.get(supposedUserId);
        filmStorage.deleteLike(film.getId(), user.getId());
    }

    /**
     * Возвращает список популярных фильмов у пользователей
     * @param supposedCount количество фильмов
     * @return список
     */
    public List<Film> getPopular(String supposedCount) {
        Integer count = idFromString(supposedCount);
        if (count == Integer.MIN_VALUE){
            count = 10;
        }
        return filmStorage.getPopular(count);
    }

    /**
     * Преобразование строки в число
     * @see #getFilmFromData(String)
     *
     * @param supposedId Строка
     * @return число
     */
    private Integer idFromString(String supposedId) {
        try {
            return Integer.valueOf(supposedId);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Возвращает фильм, после всех проверок
     * @see #idFromString(String)
     *
     * @param supposedId предполагаемый уин фильма в строке
     * @return пользователь
     * @see #get(String)
     */
    private Film getFilmFromData(String supposedId) {
        Integer filmId = idFromString(supposedId);
        if (filmId == Integer.MIN_VALUE) {
            throw new WrongIdException("Распознать уин фильма не удалось: id " + supposedId);
        }
        Film film = filmStorage.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм не найден: id:" + filmId + " отсутствует");
        }
        return film;
    }
}