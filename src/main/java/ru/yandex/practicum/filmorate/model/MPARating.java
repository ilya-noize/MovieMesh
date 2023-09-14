package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public final class MPARating {
    @Positive
    private final Long id;
    @NotBlank
    @Size(min = 1, max = 10)
    private final String name;
    @NotNull
    @Size(max = 100)
    private final String description;
}
