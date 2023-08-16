package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

@Slf4j
@Service
public class GenreService extends MasterService<Genre> {
    @Autowired
    public GenreService(MasterStorage<Genre> storage) {
        super(storage);
    }
}
