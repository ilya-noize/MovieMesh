package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class GenreControllerTest {
    private final MockMvc mockMvc;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get("/genres"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "[{\"id\":1,\"name\":\"Комедия\"}," +
                                "{\"id\":2,\"name\":\"Драма\"}," +
                                "{\"id\":3,\"name\":\"Мультфильм\"}," +
                                "{\"id\":4,\"name\":\"Триллер\"}," +
                                "{\"id\":5,\"name\":\"Документальный\"}," +
                                "{\"id\":6,\"name\":\"Боевик\"}]")));
    }

    @Test
    void getById() throws Exception {
        mockMvc.perform(get("/genres/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(
                        content().string(containsString("{\"id\":1,\"name\":\"Комедия\"}"))
                );
    }
}