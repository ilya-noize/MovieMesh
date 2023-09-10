package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Data
public final class Genre {
    @Positive
    private final Long id;
    @NotBlank
    private final String name;
}
