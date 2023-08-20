package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.LikesFilm;
import ru.yandex.practicum.filmorate.model.User;

@Service
@Slf4j
public class LikeService extends MasterService<LikesFilm> {
    private final MasterStorageDAO<Film> filmStorage;
    private final MasterStorageDAO<User> userStorage;

    @Autowired
    public LikeService(MasterStorageDAO<LikesFilm> storage,
                       MasterStorageDAO<Film> filmService,
                       MasterStorageDAO<User> userStorage) {
        super(storage);
        this.filmStorage = filmService;
        this.userStorage = userStorage;
    }

    @Override
    public LikesFilm create(LikesFilm like) {
        log.info("[+] like:{}", like);
        isExist(like);
        return super.create(like);
    }

    @Override
    public void delete(Long... id) {
        log.info("[-] like:{}", (Object[]) id);

        Long filmId = id[0];
        Long userId = id[1];

        isExist(new LikesFilm(filmId, userId));

        super.delete(filmId, userId);
    }

    private void isExist(LikesFilm like) {
        log.info("[?]: Like:{}", like);
        filmStorage.isExist(like.getFilmId());
        userStorage.isExist(like.getUserId());
    }
}
