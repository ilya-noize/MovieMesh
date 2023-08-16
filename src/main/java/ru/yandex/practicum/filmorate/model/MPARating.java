package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MPARating {
    @Positive
    Long id;
    @NotNull
    @Size(min = 1, max = 10)
    String name;
    @Size(max = 100)
    String description;
}
