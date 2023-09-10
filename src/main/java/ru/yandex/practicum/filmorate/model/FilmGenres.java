package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public final class FilmGenres {
    @Positive
    @NotNull
    private final Long filmId;
    @Positive
    @NotNull
    private final Genre genre;
}
