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
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film {
    private static final String RELEASE_DATE_LIMIT = "1895-12-28";
    @EqualsAndHashCode.Exclude
    @Positive(message = "The movie ID is a positive natural number.")
    private Long id;
    @NotNull(message = "The name of the movie cannot be null.")
    @NotBlank(message = "The title of the movie cannot be blank.")
    private String name;
    @Size(max = 200,
            message = "The description length is no more than 200 characters.")
    private String description;
    @Positive(message = "The duration of the movie is a positive natural number.")
    private Integer duration;
    @CorrectReleaseDate(value = RELEASE_DATE_LIMIT,
            message = "Release date no earlier than 28 DEC 1895 and no later than today")
    private LocalDate release;
    @Positive
    private Long mpaRatingId;
    private Set<Long> likes;

    public Film(String name, String description, LocalDate release, int duration) {
        this.name = name;
        this.description = description;
        this.release = release;
        this.duration = duration;
        this.likes = new HashSet<>();
    }
}