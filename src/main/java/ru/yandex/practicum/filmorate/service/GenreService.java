package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDAO;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDAO genreDAO;

    public Genre get(Long id) {
        return genreDAO.get(id);
    }

    public Map<Long, List<Genre>> getFilmGenres(Set<Long> filmIds) {
        return genreDAO.getFilmGenres(filmIds);
    }

    public List<Genre> getAll() {
        return genreDAO.getAll();
    }
}
