package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.constraint.CorrectReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class Film {
    public static final String RELEASE_DATE_LIMIT = "1895-12-28";
    @EqualsAndHashCode.Exclude
    @Positive(message = "The movie ID is a positive natural number.")
    private Long id;
    @NotBlank(message = "The title of the movie cannot be blank.")
    private String name;
    @NotNull
    @Size(max = 200, message = "The description length is no more than 200 characters.")
    private String description;
    @CorrectReleaseDate(value = RELEASE_DATE_LIMIT)
    private LocalDate releaseDate;
    @Positive(message = "The duration of the movie is a positive natural number.")
    private Integer duration;
    @NotNull
    private MPARating mpa;
    private List<Genre> genres;

    public Film(
            String name,
            String description,
            LocalDate releaseDate,
            int duration,
            MPARating mpa,
            List<Genre> genres
    ) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }
}
