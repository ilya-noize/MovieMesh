package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class LikesFilm {
    @Positive
    @NotNull
    private Long filmId;
    @Positive
    @NotNull
    private Long userId;
}
