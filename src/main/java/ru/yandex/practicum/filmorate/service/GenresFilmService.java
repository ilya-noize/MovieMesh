package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.model.GenresFilm;

@Service
@Slf4j
public class GenresFilmService extends MasterService<GenresFilm> {
    @Autowired
    public GenresFilmService(MasterStorageDAO<GenresFilm> storage) {
        super(storage);
    }
}
