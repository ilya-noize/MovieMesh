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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest (FilmController.class)
class FilmorateApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmController filmController;

    @Test
    public void greetingShouldReturnMessageFromService() throws Exception {
		LocalDate date = LocalDate.of(2000, 1, 1);
        Film film = new Film("Film", "Description", date, 100);
		film.setId(1L);
		filmController.create(film);
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
        assertThat(filmController).isNotNull();
    }
}