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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
    private static MockHttpServletRequestBuilder requestBuilder;
    @Autowired
    MockMvc mockMvc;
    StarWarsContent content = new StarWarsContent();

    @AfterEach
    void clearStatic() {
        requestBuilder = null;
    }

    /**
     * @throws Exception If the context drops, terminate the application
     */
    @Test
    void getAll() throws Exception {
        mockMvc.perform(request(GET, "/films"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    @Test
    void create() throws Exception {
        Film film = content.film("film1");

        requestBuilder = post("/films")
                .param("\"name\"", "\"" + film.getName() + "\"")
                .param("\"description\"", "\"" + film.getDescription() + "\"")
                .param("\"releaseDate\"", "\"" + film.getReleaseDate().toString() + "\"")
                .param("\"duration\"", "\"" + film.getDuration().toString() + "\"")
                .param("\"mpa\"", "\"" + film.getMpa().toString() + "\"")
                .param("\"genres\"", "\"" + film.getGenres().toString() + "\"");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update() throws Exception {
        create();
        LinkedHashSet<Genre> genreList = new LinkedHashSet<>(List.of(content.genre(4), content.genre(5)));
        Film film = new Film(1L, "StarWars:Episode I", "A long time ago in a galaxy far, far away", content.release(0), 120, content.mpa(1), genreList);
        requestBuilder = put("/films")
                .param("\"id\"", "\"1\"")
                .param("\"name\"", "\"" + film.getName() + "\"")
                .param("\"description\"", "\"" + film.getDescription() + "\"")
                .param("\"releaseDate\"", "\"" + film.getReleaseDate().toString() + "\"")
                .param("\"duration\"", "\"" + film.getDuration().toString() + "\"")
                .param("\"mpa\"", "\"" + film.getMpa().toString() + "\"")
                .param("\"genres\"", "\"" + film.getGenres().toString() + "\"");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(request(GET, "/films/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Film not found - id:1 not exist")));
    }

    @Test
    void addLike() throws Exception {
        mockMvc.perform(request(PUT, "/films/1/like/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteLike() throws Exception {
        mockMvc.perform(request(DELETE, "/films/1/like/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getPopular() throws Exception {
        mockMvc.perform(request(GET, "/films/popular"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    static final class StarWarsContent {
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
                "film1", new Film(1L, "StarWars:Episode I", "A long time ago in a galaxy far, far away", releases.get(0), 120, mpa.get(1), new LinkedHashSet<>()),
                "film2", new Film(2L, "StarWars:Episode II", "A long time ago in a galaxy far, far away", releases.get(1), 120, mpa.get(1), new LinkedHashSet<>()),
                "emptyName", new Film(3L, "", "A long time ago in a galaxy far, far away", releases.get(2), 120, mpa.get(1), new LinkedHashSet<>()),
                "nullName", new Film(4L, null, "A long time ago in a galaxy far, far away", releases.get(3), 120, mpa.get(1), new LinkedHashSet<>()),
                "moreLimitDescription", new Film(5L, "StarWars:Episode V", ("A long time ago in a galaxy far, far away").repeat(5), releases.get(4), 120, mpa.get(1), new LinkedHashSet<>()),
                "wrongReleaseOverLimit", new Film(6L, "StarWars:Episode VI", "A long time ago in a galaxy far, far away", releases.get(5), 120, mpa.get(1), new LinkedHashSet<>()),
                "wrongReleaseInFuture", new Film(7L, "StarWars:Episode VII", "A long time ago in a galaxy far, far away", releases.get(6), 120, mpa.get(1), new LinkedHashSet<>()),
                "negativeDuration", new Film(8L, "StarWars:Episode VIII", "A long time ago in a galaxy far, far away", releases.get(7), 120, mpa.get(1), new LinkedHashSet<>()),
                "wrongId", new Film(9999L, "StarWars:Episode IX", "A long time ago in a galaxy far, far away", releases.get(8), 120, mpa.get(1), new LinkedHashSet<>())
        );

        public LocalDate release(int index) {
            return releases.get(index);
        }

        public MPARating mpa(int index) {
            return mpa.get(index);
        }

        public Genre genre(int index) {
            return genres.get(index);
        }

        public Film film(String classification) {
            return films.get(classification);
        }
    }
}
