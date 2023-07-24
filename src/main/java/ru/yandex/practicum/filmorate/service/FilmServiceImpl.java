package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FailSetFilmLikesException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final UserService userService;
    private final FilmStorage filmStorage;
    private final Comparator<Film> SORT_LIKES = Comparator.comparing(film -> film.getLikes().size());

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
        log.info("Update Film({})", film.getId());
        return filmStorage.update(film);
    }

    /**
     * Возвращает фильм
     *
     * @param supposedId уин фильма
     * @return фильм
     */
    public Film get(Long supposedId) {
        Film film = filmStorage.get(supposedId);
        log.info("get Film({})", supposedId);
        return film;
    }

    /**
     * Возвращает список фильмов
     *
     * @return список
     */
    public List<Film> getAll() {
        log.info("get all Films");
        return filmStorage.getAll();
    }

    /**
     * Возвращает список популярных фильмов у пользователей
     *
     * @param supposedCount количество фильмов
     * @return список
     */
    public List<Film> getPopular(Long supposedCount) {
        log.info("* Возвращаем ТОП-{} популярных фильмов у пользователей", supposedCount);
        return getAll().stream()
                .sorted(SORT_LIKES.reversed())
                .limit(supposedCount)
                .collect(Collectors.toList());
    }

    /**
     * Добавляет лайк от пользователя
     *
     * @param supposedId     уин фильма
     * @param supposedUserId уин пользователя
     */
    public void addLike(Long supposedId, Long supposedUserId) {
        Film film = get(supposedId);
        Set<Long> likes = film.getLikes();

        User user = userService.get(supposedUserId);
        Long userId = user.getId();

        log.info("* Добавляем лайк пользователя {} фильму {}", user.getLogin(), film.getName());

        if (likes.contains(userId)) {
            String error = String.format("Пользователь %s уже поставил лайк фильму %s", user.getLogin(), film.getName());
            log.error(error);
            throw new FailSetFilmLikesException(error);
        }

        likes.add(userId);
        film.setLikes(likes);
    }

    /**
     * Удаляет лайк пользователя
     *
     * @param supposedId     уин фильма
     * @param supposedUserId уин пользователя
     */
    public void deleteLike(Long supposedId, Long supposedUserId) {
        Film film = get(supposedId);
        Set<Long> likes = film.getLikes();

        User user = userService.get(supposedUserId);
        Long userId = user.getId();

        log.info("* Удаляем лайк пользователя {} фильму {}", user.getLogin(), film.getName());

        if (!likes.contains(userId)) {
            String error = String.format("Пользователь %s не ставил лайк фильму %s",
                    film.getName(),
                    user.getLogin());
            log.error(error);
            throw new FailSetFilmLikesException(error);
        }

        likes.remove(userId);
        film.setLikes(likes);
    }
}