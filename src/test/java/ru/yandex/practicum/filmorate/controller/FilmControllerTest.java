package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    private static MockHttpServletRequestBuilder requestBuilder;
    private final List<LocalDate> releases = List.of(
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
    private final List<MPARating> mpa = List.of(
            new MPARating(1L, "G", "Нет возрастных ограничений"),
            new MPARating(2L, "PG", "Рекомендуется присутствие родителей"),
            new MPARating(3L, "PG-13", "Детям до 13 лет просмотр не желателен"),
            new MPARating(4L, "R", "Лицам до 17 лет обязательно присутствие взрослого"),
            new MPARating(5L, "NC-17", "Лицам до 18 лет просмотр запрещен")
    );
    private final List<Genre> genres = List.of(
            new Genre(1L, "Комедия"),
            new Genre(2L, "Драма"),
            new Genre(3L, "Мультфильм"),
            new Genre(4L, "Триллер"),
            new Genre(5L, "Документальный"),
            new Genre(6L, "Боевик")
    );
    private final Map<String, Film> films = Map.of(
            "film1", new Film(1L, "StarWars:Episode I", "A long time ago in a galaxy far, far away", releases.get(0), 120, mpa.get(1), new ArrayList<>()),
            "film2", new Film(2L, "StarWars:Episode II", "A long time ago in a galaxy far, far away", releases.get(1), 120, mpa.get(1), new ArrayList<>()),
            "emptyName", new Film(3L, "", "A long time ago in a galaxy far, far away", releases.get(2), 120, mpa.get(1), new ArrayList<>()),
            "nullName", new Film(4L, null, "A long time ago in a galaxy far, far away", releases.get(3), 120, mpa.get(1), new ArrayList<>()),
            "moreLimitDescription", new Film(5L, "StarWars:Episode V", ("A long time ago in a galaxy far, far away").repeat(5), releases.get(4), 120, mpa.get(1), new ArrayList<>()),
            "wrongReleaseOverLimit", new Film(6L, "StarWars:Episode VI", "A long time ago in a galaxy far, far away", releases.get(5), 120, mpa.get(1), new ArrayList<>()),
            "wrongReleaseInFuture", new Film(7L, "StarWars:Episode VII", "A long time ago in a galaxy far, far away", releases.get(6), 120, mpa.get(1), new ArrayList<>()),
            "negativeDuration", new Film(8L, "StarWars:Episode VIII", "A long time ago in a galaxy far, far away", releases.get(7), 120, mpa.get(1), new ArrayList<>()),
            "wrongId", new Film(9999L, "StarWars:Episode IX", "A long time ago in a galaxy far, far away", releases.get(8), 120, mpa.get(1), new ArrayList<>())
    );
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void clearStatic() {
        requestBuilder = null;
    }

    @Test
    void getAll() throws Exception {
        requestBuilder = get("/films");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    void create() throws Exception {
        Film film = films.get("film1");

        requestBuilder = post("/films")
                .param(inQuotes("name"), inQuotes(film.getName()))
                .param(inQuotes("description"), inQuotes(film.getDescription()))
                .param(inQuotes("releaseDate"), inQuotes(film.getReleaseDate().toString()))
                .param(inQuotes("duration"), inQuotes(film.getDuration().toString()))
                .param(inQuotes("mpa"), inQuotes(film.getMpa().toString()))
                .param(inQuotes("genres"), inQuotes(film.getGenres().toString()));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void update() throws Exception {
        Film film = films.get("film1");
        film.setGenres(List.of(genres.get(4), genres.get(5)));
        requestBuilder = put("/films")
                .param(inQuotes("name"), inQuotes(film.getName()))
                .param(inQuotes("description"), inQuotes(film.getDescription()))
                .param(inQuotes("releaseDate"), inQuotes(film.getReleaseDate().toString()))
                .param(inQuotes("duration"), inQuotes(film.getDuration().toString()))
                .param(inQuotes("mpa"), inQuotes(film.getMpa().toString()))
                .param(inQuotes("genres"), inQuotes(film.getGenres().toString()));

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void get1() throws Exception {
        requestBuilder = get("/films/1");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Film not found - id:1 not exist")));
    }

    @Test
    void addLike() throws Exception {
        requestBuilder = put("/films/1/like/1");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLike() throws Exception {
        requestBuilder = delete("/films/1/like/1");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getPopular() throws Exception {
        requestBuilder = get("/films/popular");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());

    }

    private String inQuotes(String s) {
        return String.format("\"%s\"", s);
    }
}