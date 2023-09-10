package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.LikeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;
    private final LikeService likeService;

    @PostMapping
    public Film create(@RequestBody @Valid Film film) {
        log.debug("[+] Film\n Film:{}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.debug("[u] Film\n Film:{}", film);
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable Long id) {
        log.debug("[>] Film id:{}", id);
        return filmService.get(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.debug("[+] Like\n Like [Film filmId:{} from User filmId:{}]", filmId, userId);
        likeService.add(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.debug("[-] Like\n Like [Film filmId:{} from User filmId:{}]", filmId, userId);
        likeService.delete(filmId, userId);
    }

    @GetMapping
    public List<Film> getAll() {
        log.debug("[>] All Films");
        return filmService.getAll();
    }

    @GetMapping({"/popular?count={count}", "/popular"})
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Long count) {
        log.debug("[>] PopularFilms TOP-{}", count);
        return filmService.getPopular(count);
    }
}