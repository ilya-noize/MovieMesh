package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.rowMapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmGenres;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public final class GenreDAO {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;
    private final FilmGenresDAO filmGenresDAO;

    public Genre get(Long id) {
        String error = String.format("Genre not found - id:%d not exist", id);
        String sql = "SELECT * FROM GENRES WHERE ID = ?";

        return jdbcTemplate.query(sql, genreRowMapper, id)
                .stream().findFirst()
                .orElseThrow(new NotFoundException(error));
    }

    public Map<Long, List<Genre>> getFilmGenres(Set<Long> filmIds) {
        Map<Long, List<Genre>> filmsGenres = new HashMap<>();
        filmIds.forEach(id -> filmsGenres.put(id, new ArrayList<>()));

        for (FilmGenres filmGenres : filmGenresDAO.getAllFilmGenres(filmIds)) {
            Long forFilmId = filmGenres.getFilmId();
            filmsGenres.get(forFilmId).add(filmGenres.getGenre());
        }

        return filmsGenres;
    }

    public List<Genre> getAll() {
        String sql = "SELECT * FROM GENRES ORDER BY ID";
        return jdbcTemplate.query(sql, genreRowMapper);
    }
}
