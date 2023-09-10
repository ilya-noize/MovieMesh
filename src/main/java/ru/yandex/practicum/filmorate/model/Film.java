package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.constraint.CorrectReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public final class Film {
    public static final String RELEASE_DATE_LIMIT = "1895-12-28";
    @EqualsAndHashCode.Exclude
    @Positive(message = "The movie ID is a positive natural number.")
    private final Long id;
    @NotBlank(message = "The title of the movie cannot be blank.")
    private final String name;
    @NotNull
    @Size(max = 200, message = "The description length is no more than 200 characters.")
    private final String description;
    @CorrectReleaseDate(value = RELEASE_DATE_LIMIT)
    private final LocalDate releaseDate;
    @Positive(message = "The duration of the movie is a positive natural number.")
    private final Integer duration;
    @NotNull
    private MPARating mpa;
    private List<Genre> genres;
}
