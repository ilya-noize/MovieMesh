package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class FilmorateApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmController filmController;

    private static void getSqlInsertInto() {
        final List<LocalDate> releases = List.of(
                LocalDate.of(1999, 5, 19),
                LocalDate.of(2002, 5, 16),
                LocalDate.of(2005, 5, 19),
                LocalDate.of(1977, 5, 25),
                LocalDate.of(1980, 5, 21),
                LocalDate.of(1883, 5, 25),
                LocalDate.of(3015, 12, 18),
                LocalDate.of(2017, 12, 15),
                LocalDate.of(2019, 12, 19)
        );
        final List<MPARating> mpaRatings = List.of(
                new MPARating(1L, "G", "Нет возрастных ограничений"),
                new MPARating(2L, "PG", "Рекомендуется присутствие родителей"),
                new MPARating(3L, "PG-13", "Детям до 13 лет просмотр не желателен"),
                new MPARating(4L, "R", "Лицам до 17 лет обязательно присутствие взрослого"),
                new MPARating(5L, "NC-17", "Лицам до 18 лет просмотр запрещен")
        );
        final Map<String, Film> films = Map.of(
                "film1", new Film(1L, "StarWars:Episode I", releases.get(0), "A long time ago in a galaxy far, far away", 120, mpaRatings.get(1), new ArrayList<>()),
                "film2", new Film(2L, "StarWars:Episode II", releases.get(1), "A long time ago in a galaxy far, far away", 120, mpaRatings.get(1), new ArrayList<>()),
                "emptyName", new Film(3L, "", releases.get(2), "A long time ago in a galaxy far, far away", 120, mpaRatings.get(1), new ArrayList<>()),
                "nullName", new Film(4L, null, releases.get(3), "A long time ago in a galaxy far, far away", 120, mpaRatings.get(1), new ArrayList<>()),
                "moreLimitDescription", new Film(5L, "StarWars:Episode V", releases.get(4), ("A long time ago in a galaxy far, far away").repeat(5), 120, mpaRatings.get(1), new ArrayList<>()),
                "wrongReleaseOverLimit", new Film(6L, "StarWars:Episode VI", releases.get(5), "A long time ago in a galaxy far, far away", 120, mpaRatings.get(1), new ArrayList<>()),
                "wrongReleaseInFuture", new Film(7L, "StarWars:Episode VII", releases.get(6), "A long time ago in a galaxy far, far away", 120, mpaRatings.get(1), new ArrayList<>()),
                "negativeDuration", new Film(8L, "StarWars:Episode VIII", releases.get(7), "A long time ago in a galaxy far, far away", -120, mpaRatings.get(1), new ArrayList<>()),
                "wrongId", new Film(9999L, "StarWars:Episode IX", releases.get(8), "A long time ago in a galaxy far, far away", 120, mpaRatings.get(1), new ArrayList<>())
        );
        System.out.println("INSERT INTO USERS (ID, NAME, DESCRIPTION, DURATION, RELEASEDATE, MPA_RATING_ID) VALUES");

        films.forEach((key, film) -> System.out.println("-- Test Film:" + key + "\n"
                                + String.format("(%d, \"%s\", \"%s\", %d, to_date(\"%s\", \"YYYY.MM.DD\"), %d),",
                                film.getId(),
                                film.getName(),
                                film.getDescription(),
                                film.getDuration(),
                                film.getReleaseDate(),
                                film.getMpa().getId()
                        )
                )
        );
        System.out.println("-".repeat(80));
        System.out.println("INSERT INTO MPA_RATING(ID, NAME, DESCRIPTION) VALUES");
        mpaRatings.forEach(mpa -> System.out.printf("(%d, \"%s\", \"%s\"),%n",
                mpa.getId(),
                mpa.getName(),
                mpa.getDescription())
        );
    }

    @Test
    void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/films"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    void contextLoads() {
//        getSqlInsertInto();
        assertThat(filmController).isNotNull();
    }
}