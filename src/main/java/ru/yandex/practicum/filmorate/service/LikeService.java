package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

@Service
@Slf4j
public class LikeService extends MasterService<Like> {
    private final MasterStorageDAO<Film> filmStorage;
    private final MasterStorageDAO<User> userStorage;

    @Autowired
    public LikeService(MasterStorageDAO<Like> storage,
                       MasterStorageDAO<Film> filmService,
                       MasterStorageDAO<User> userStorage) {
        super(storage);
        this.filmStorage = filmService;
        this.userStorage = userStorage;
    }

    @Override
    public Like create(Like like) {
        log.info("[+] like:{}", like);
        if (isExist(like)) {
            super.create(like);
            return like;
        }
        return null;
    }

    @Override
    public void delete(Long... id) {
        log.info("[-] like:{}", (Object[]) id);
        Like like = getLike(id[0], id[1]);
        if (isExist(like)) {
            super.delete(like.getFilmId(), like.getUserId());
        }
    }

    private Like getLike(Long filmId, Long userId) {
        return new Like(filmId, userId);
    }

    private boolean isExist(Like like) {
        Long filmId = like.getFilmId();
        log.info("Check: Film id:{}, User id:{}", filmId, like.getUserId());
        return filmService.isExist(filmId);
    }
}
