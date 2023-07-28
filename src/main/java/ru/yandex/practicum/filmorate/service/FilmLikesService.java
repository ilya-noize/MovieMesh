package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FailSetFilmLikesException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmLikesService extends FilmService {

    private final MasterStorage<User> userStorage;
    private final Comparator<Film> sortLikes = Comparator.comparing(film -> film.getLikes().size());

    @Autowired
    protected FilmLikesService(MasterStorage<Film> filmStorage, MasterStorage<User> userStorage) {
        super(filmStorage);
        this.userStorage = userStorage;
    }

    /**
     * Возвращает список популярных фильмов у пользователей
     *
     * @param supposedCount количество фильмов
     * @return список
     */
    public List<Film> getPopular(Long supposedCount) {
        log.info("* Возвращаем ТОП-{} популярных фильмов у пользователей", supposedCount);
        return this.getAll().stream()
                .sorted(sortLikes.reversed())
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
        Film film = this.get(supposedId);
        Set<Long> likes = film.getLikes();

        User user = userStorage.get(supposedUserId);
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
        Film film = this.get(supposedId);
        Set<Long> likes = film.getLikes();

        User user = userStorage.get(supposedUserId);
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