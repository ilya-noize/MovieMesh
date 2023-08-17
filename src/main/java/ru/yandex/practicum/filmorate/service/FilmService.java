package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresFilm;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
public class FilmService extends MasterService<Film> {
    private static Long filmId;
    private final MasterStorageDAO<GenresFilm> genresFilmStorage;
    private final Comparator<Film> sortLikes = Comparator.comparing(film -> film.getLikes().size());

    public FilmService(MasterStorageDAO<Film> storage,
                       MasterStorageDAO<GenresFilm> genresFilmStorage) {
        super(storage);
        this.genresFilmStorage = genresFilmStorage;
    }

    /**
     * Возвращает список популярных фильмов у пользователей
     *
     * @param supposedCount количество фильмов
     * @return список
     */
    public List<Film> getPopular(Long supposedCount) {
        log.info("Returning the TOP{} popular movies from users", supposedCount);
        return this.getAll().stream()
                .sorted(sortLikes)
                .limit(supposedCount)
                .collect(toList());
    }

    @Override
    public Film update(Film film) {
        log.info("[i] service: update film:{}", film);
        filmId = film.getId();

        Set<Long> genresFilmOn = filterAllGenresFilms(genresFilmStorage.getAll());
        Set<Long> genresFilmIn = getSetGenreIds(film.getGenres());

        updateGenresFilm(genresFilmOn, genresFilmIn);

        return super.update(film);
    }

    private Set<Long> filterAllGenresFilms(List<GenresFilm> getAll) {
        return getAll.stream()
                .filter(genresFilm -> genresFilm.getFilmId().equals(filmId))
                .map(GenresFilm::getId)
                .collect(toSet());
    }

    private Set<Long> getSetGenreIds(Set<Genre> genres) {
        return genres.stream()
                .map(Genre::getId)
                .collect(toSet());
    }

    private void updateGenresFilm(Set<Long> genresFilmOn,
                                  Set<Long> genresFilmIn) {
        log.info("[i] service: update Genres for film id:{}", filmId);
        if (genresFilmOn.size() > genresFilmIn.size()) {
            genresFilmOn.retainAll(genresFilmIn);
            deleteGenres(genresFilmOn);
        } else {
            genresFilmIn.retainAll(genresFilmOn);
            addGenres(genresFilmIn);
        }
    }

    private void deleteGenres(Set<Long> genresFilmOn) {
        genresFilmOn.forEach(
                genresFilm -> genresFilmStorage.delete(
                        filmId, genresFilm
                )
        );
    }

    private void addGenres(Set<Long> genresFilmIn) {
        genresFilmIn.forEach(
                genresId -> genresFilmStorage.create(
                        new GenresFilm(filmId, genresId)
                )
        );
    }
}
