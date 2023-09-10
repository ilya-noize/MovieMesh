package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MPARatingDAO;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MPARatingService {
    private final MPARatingDAO mpaRatingDAO;

    public MPARating get(Long id) {
        log.debug("[>][S] MPARating id:{}", id);
        return mpaRatingDAO.get(id);
    }

    public List<MPARating> getAll() {
        log.debug("[>][S] All MPARating");
        return mpaRatingDAO.getAll();
    }
}
