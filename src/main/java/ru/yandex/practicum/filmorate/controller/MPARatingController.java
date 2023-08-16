package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.service.MPARatingService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MPARatingController {
    private final MPARatingService mpaRatingService;

    @GetMapping
    public List<MPARating> getAll() {
        log.info("[>] All MPARating");
        return mpaRatingService.getAll();
    }

    @GetMapping("/{id}")
    public MPARating get(@PathVariable Long id) {
        log.info("[>] MPARating id:{}", id);
        return mpaRatingService.get(id);
    }
}
