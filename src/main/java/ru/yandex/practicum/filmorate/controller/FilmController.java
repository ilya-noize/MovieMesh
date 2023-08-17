package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;
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
        log.info("[+] Film:{}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody @Valid Film film) {
        log.info("[u] Film:{}", film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        Like like = new Like(id, userId);
        log.info("[+] Like [{}]", like);
        likeService.create(like);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        log.info("[-] Like [Film id:{} from User id:{}]", id, userId);
        likeService.delete(id, userId);
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("[>] All Films");
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film get(@PathVariable Long id) {
        log.info("[>] Film id:{}", id);
        return filmService.get(id);
    }

    @GetMapping({"/popular?count={count}", "/popular"})
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Long count) {
        log.info("[>] PopularFilms TOP-{}", count);
        return filmService.getPopular(count);
    }
}