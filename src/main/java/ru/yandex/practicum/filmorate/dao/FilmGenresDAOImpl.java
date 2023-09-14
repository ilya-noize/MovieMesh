package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.FilmGenres;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public final class FilmGenresDAOImpl implements FilmGenresDAO {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<FilmGenres> filmGenresRowMapper;

    @Override
    public void add(Long filmId, List<Genre> genres) {
        log.debug("[i] ADD FilmGENRE\n filmId:{}, genres:{}\n execute:\n", filmId, genres);
        String sql = "MERGE INTO FILM_GENRES KEY(FILM_ID, GENRE_ID) VALUES (?, ?);";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long genreId = genres.get(i).getId();
                log.debug("\t filmId:{}\t genreId:{}\n", filmId, genreId);
                ps.setLong(1, filmId);
                ps.setLong(2, genreId);
            }

            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    @Override
    public void update(Long filmId, List<Genre> genres) {
        delete(filmId);
        add(filmId, genres);
    }


    @Override
    public Map<Long, List<Genre>> getFilmGenres(Set<Long> filmIds) {
        Map<Long, List<Genre>> filmsGenres = new HashMap<>();
        filmIds.forEach(id -> filmsGenres.put(id, new ArrayList<>()));

        for (FilmGenres filmGenres : getAllFilmGenres(filmIds)) {
            Long forFilmId = filmGenres.getFilmId();
            filmsGenres.get(forFilmId).add(filmGenres.getGenre());
        }

        return filmsGenres;
    }

    @Override
    public List<FilmGenres> getAllFilmGenres(Set<Long> filmIds) {
        String inSql = String.join(",", Collections.nCopies(filmIds.size(), "?"));
        String sql = String.format("SELECT F.ID FILM_ID, G.ID GENRE_ID, G.NAME GENRE_NAME"
                + " FROM FILM_GENRES GF"
                + " RIGHT JOIN FILMS F ON F.id = GF.FILM_ID"
                + " RIGHT JOIN GENRES G ON G.id = GF.GENRE_ID"
                + " WHERE F.ID IN (%s) ORDER BY G.ID, G.NAME;", inSql);

        return jdbcTemplate.query(sql, filmGenresRowMapper, filmIds.toArray());
    }

    private void delete(Long filmId) {
        log.debug("[i] deleteGenreFilm\n filmId:{}", filmId);
        String sql = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }
}
