package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.constraints.CorrectReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Film {
    private static final String RELEASE_DATE_LIMIT = "1895-12-28";

    @EqualsAndHashCode.Exclude
    private Integer id;

    @NotNull(message = "Название фильма не может быть null.")
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(max = 200, message = "Длина описания не более 200 символов.")
    private String description;

    @CorrectReleaseDate(value = RELEASE_DATE_LIMIT, message = "Дата релиза не раньше 28 DEC 1895 и не позже сегодня")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма - положительное натуральное число.")
    private int duration;

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}