package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.model.Genre;

@Slf4j
@Service
public class GenreService extends MasterService<Genre> {
    @Autowired
    public GenreService(MasterStorageDAO<Genre> storage) {
        super(storage);
    }
}
