package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.rowMapper.FilmGenresRowMapper;
import ru.yandex.practicum.filmorate.model.FilmGenres;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public final class FilmGenresDAO {
    private final JdbcTemplate jdbcTemplate;
    private final FilmGenresRowMapper filmGenresRowMapper;

    public void add(Long filmId, List<Genre> genres) {
        for (Genre genre : genres) {
            log.info("[i] ADD FilmGENRE\tfilmId:{}\tgenreId:{}", filmId, genre.getId());
            String sql = "MERGE INTO FILM_GENRES KEY(FILM_ID, GENRE_ID) VALUES (?, ?);";
            jdbcTemplate.update(sql, filmId, genre.getId());
        }
    }

    public void delete(Long id) {
        log.info("[i] deleteGenreFilm\n id:{}", id);
        String sql = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<FilmGenres> getAllFilmGenres(Set<Long> filmIds) {
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));
        String sql = String.format("SELECT F.ID FILM_ID, G.ID GENRE_ID, G.NAME GENRE_NAME"
                + " FROM FILM_GENRES GF"
                + " RIGHT JOIN FILMS F ON F.id = GF.FILM_ID"
                + " RIGHT JOIN GENRES G ON G.id = GF.GENRE_ID"
                + " WHERE F.ID IN (%s) ORDER BY G.ID, G.NAME;", inSql);

        return jdbcTemplate.query(sql, filmGenresRowMapper, filmIds.toArray());
    }
}
