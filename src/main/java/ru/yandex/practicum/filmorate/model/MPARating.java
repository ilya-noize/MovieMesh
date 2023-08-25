package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MPARating {
    @Positive
    private Long id;
    @NotBlank
    @Size(min = 1, max = 10)
    private String name;
    @NotNull
    @Size(max = 100)
    private String description;
}
