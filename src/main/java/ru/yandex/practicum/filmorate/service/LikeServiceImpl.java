package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDAO;
import ru.yandex.practicum.filmorate.dao.FilmLikesDAO;
import ru.yandex.practicum.filmorate.dao.UserDAO;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final FilmLikesDAO likeDAO;
    private final UserDAO userDAO;
    private final FilmDAO filmDAO;

    @Override
    public void add(Long filmId, Long userId) {
        log.debug("[+] like to:{} from:{}", filmId, userId);
        isExist(filmId, userId);
        likeDAO.add(filmId, userId);
    }

    @Override
    public void delete(Long filmId, Long userId) {
        log.debug("[-] like to:{} from:{}", filmId, userId);
        isExist(filmId, userId);
        likeDAO.delete(filmId, userId);
    }

    private void isExist(Long filmId, Long userId) {
        log.debug("[?]: Like to:{} from:{}", filmId, userId);
        if (filmDAO.isExist(filmId).equals(0L)) {
            String error = String.format("Film not found - id:%d not exist", filmId);
            log.error(error);
            throw new NotFoundException(error);
        }
        if (userDAO.isExist(userId).equals(0L)) {
            String error = String.format("User not found - id:%d not exist", userId);
            log.error(error);
            throw new NotFoundException(error);
        }
    }
}
