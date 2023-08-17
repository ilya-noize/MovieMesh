package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MasterStorageDAO;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.GenresFilm;

@Service
@Slf4j
public class GenresFilmService extends MasterService<GenresFilm> {
    private final MasterStorageDAO<Film> filmStorage;
    private final MasterStorageDAO<Genre> genreStorage;

    @Autowired
    public GenresFilmService(MasterStorageDAO<GenresFilm> storage,
                             MasterStorageDAO<Film> filmService,
                             MasterStorageDAO<Genre> genreService) {
        super(storage);
        this.filmStorage = filmService;
        this.genreStorage = genreService;
    }

    @Override
    public GenresFilm create(GenresFilm genresFilm) {
        log.info("[+] genresFilm:{}", genresFilm);
        if (this.isExist(genresFilm)) {
            super.create(genresFilm);
            return genresFilm;
        }
        return null;
    }

    @Override
    public void delete(Long... id) {
        log.info("[-] genresFilm:{}", (Object[]) id);
        GenresFilm genresFilm = getGenresFilm(id[0], id[1]);
        if (isExist(genresFilm)) {
            super.delete(genresFilm.getFilmId(), genresFilm.getId());
        }
    }

    private GenresFilm getGenresFilm(Long filmId, Long genreId) {
        return new GenresFilm(filmId, genreId);
    }

    private boolean isExist(GenresFilm genresFilm) {
        Long filmId = genresFilm.getFilmId();
        Long genreId = genresFilm.getId();
        log.info("[?] Film id:{}, Genre id:{}", filmId, genreId);
        boolean isExistFilm = filmService.isExist(filmId);
        boolean isExistGenre = genreService.isExist(genreId);
        return isExistFilm && isExistGenre;
    }
}
