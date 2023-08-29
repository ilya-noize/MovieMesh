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
        log.info("[+] Film\n Film:{}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("[u] Film\n Film:{}", film);
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable Long id) {
        log.info("[>] Film id:{}", id);
        return filmService.get(id);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("[+] Like\n Like [Film filmId:{} from User filmId:{}]", filmId, userId);
        likeService.add(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("[-] Like\n Like [Film filmId:{} from User filmId:{}]", filmId, userId);
        likeService.delete(filmId, userId);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("[>] All Films");
        return filmService.getAll();
    }

    @GetMapping({"/popular?count={count}", "/popular"})
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Long count) {
        log.info("[>] PopularFilms TOP-{}", count);
        return filmService.getPopular(count);
    }
}