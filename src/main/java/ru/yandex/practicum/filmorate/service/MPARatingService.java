package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MasterStorage;

@Slf4j
@Service
public class MPARatingService extends MasterService<MPARating> {

    @Autowired
    public MPARatingService(MasterStorage<MPARating> storage) {
        super(storage);
    }
}
