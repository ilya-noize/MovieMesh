package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.dao.FilmLikesDAO;
import ru.yandex.practicum.filmorate.dao.UserDAO;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {
    private final FilmLikesDAO likeDAO;
    private final UserDAO userDAO;
    private final FilmDAO filmDAO;

    public void add(Long filmId, Long userId) {
        log.info("[+] like to:{} from:{}", filmId, userId);
        isExist(filmId, userId);
        likeDAO.add(filmId, userId);
    }

    public void delete(Long filmId, Long userId) {
        log.info("[-] like to:{} from:{}", filmId, userId);
        isExist(filmId, userId);
        likeDAO.delete(filmId, userId);
    }

    private void isExist(Long filmId, Long userId) {
        log.info("[?]: Like to:{} from:{}", filmId, userId);
        filmDAO.get(filmId);
        userDAO.get(userId);
    }
}
