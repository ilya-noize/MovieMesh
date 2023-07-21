package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FailSetFilmLikesException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.WrongFilmIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {
    FilmStorage filmStorage;
    UserService userService;


    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage,
                           @Autowired(required = false) UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

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
        Integer filmId = film.getId();
        if (filmStorage.get(filmId) == null) {
            String error = String.format("Фильм не найден: id:%d отсутствует", filmId);
            log.error(error);
            throw new NotFoundException(error);
        }
        return filmStorage.update(film);
    }

    /**
     * Возвращает фильм
     *
     * @param supposedId уин фильма
     * @return фильм
     */
    public Film get(String supposedId) {
        return getFilmFromData(supposedId);
    }

    /**
     * Возвращает список фильмов
     *
     * @return список
     */
    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    /**
     * Возвращает список популярных фильмов у пользователей
     *
     * @param supposedCount количество фильмов
     * @return список
     */
    public List<Film> getPopular(String supposedCount) {
        Integer count = IntegerFromString(supposedCount);
        log.info("* Возвращаем ТОП-{} популярных фильмов у пользователей", count);
        if (count == Integer.MIN_VALUE || count <= 0 || supposedCount == null) {
            count = 10;
        }
        return getAll().stream()
                .sorted((f1, f2) -> (
                    filmStorage.getLikes(f2.getId()).size() - filmStorage.getLikes(f1.getId()).size()
                ))
                .limit(count)
                .collect(Collectors.toList());
    }

    /**
     * Добавляет лайк от пользователя
     *
     * @param supposedId     уин фильма
     * @param supposedUserId уин пользователя
     */
    public void addLike(String supposedId, String supposedUserId) {
        Film film = get(supposedId);
        User user = userService.get(supposedUserId);

        log.info("* Добавляем лайк пользователя {} фильму {}", user.getLogin(), film.getName());
        Integer userId = user.getId();
        Integer filmId = film.getId();
        Set<Integer> likesFilm = filmStorage.getLikes(filmId);

        if (likesFilm.contains(userId)) {
            String error = String.format("Пользователь %s уже поставил лайк фильму %s",
                    film.getName(),
                    user.getLogin());
            log.error(error);
            throw new FailSetFilmLikesException(error);
        }

        likesFilm.add(userId);
        filmStorage.setLikes(filmId, likesFilm);
    }

    /**
     * Удаляет лайк пользователя
     *
     * @param supposedId     уин фильма
     * @param supposedUserId уин пользователя
     */
    public void deleteLike(String supposedId, String supposedUserId) {
        Film film = get(supposedId);
        User user = userService.get(supposedUserId);

        log.info("* Удаляем лайк пользователя {} фильму {}", user.getLogin(), film.getName());
        Integer filmId = film.getId();
        Integer userId = user.getId();
        Set<Integer> likesFilm = filmStorage.getLikes(filmId);

        if (!likesFilm.contains(userId)) {
            String error = String.format("Пользователь %s не ставил лайк фильму %s",
                    film.getName(),
                    user.getLogin());
            log.error(error);
            throw new FailSetFilmLikesException(error);
        }

        likesFilm.remove(userId);
        filmStorage.setLikes(filmId, likesFilm);
    }

    /**
     * Преобразование строки в число
     *
     * @param supposedInt Строка
     * @return число
     * @see #getFilmFromData(String)
     */
    private Integer IntegerFromString(String supposedInt) {
        try {
            return Integer.valueOf(supposedInt);
        } catch (NumberFormatException e) {
            return Integer.MIN_VALUE;
        }
    }

    /**
     * Возвращает фильм, после всех проверок
     *
     * @param supposedId предполагаемый уин фильма в строке
     * @return пользователь
     * @see #IntegerFromString(String)
     * @see #get(String)
     */
    private Film getFilmFromData(String supposedId) {
        Integer filmId = IntegerFromString(supposedId);
        if (filmId == Integer.MIN_VALUE || filmId <= 0) {
            String error = String.format("Неверный уин фильма: %d", filmId);
            log.error(error);
            throw new WrongFilmIdException(error);
        }
        Film film = filmStorage.get(filmId);
        if (film == null) {
            String error = String.format("Фильм не найден: id:%d отсутствует", filmId);
            log.error(error);
            throw new NotFoundException(error);
        }
        return film;
    }
}