package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
class MPARatingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * In JDK17 it would be more beautiful ^_^
     *
     * @throws Exception If the context drops, terminate the application
     */
    @Test
    void getAll() throws Exception {
        mockMvc.perform(request(GET, "/mpa"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "[{\"id\":1,\"name\":\"G\",\"description\":\"Нет возрастных ограничений\"},"
                                + "{\"id\":2,\"name\":\"PG\",\"description\":\"Рекомендуется присутствие родителей\"},"
                                + "{\"id\":3,\"name\":\"PG-13\",\"description\":\"Детям до 13 лет просмотр не желателен\"},"
                                + "{\"id\":4,\"name\":\"R\",\"description\":\"Лицам до 17 лет обязательно присутствие взрослого\"},"
                                + "{\"id\":5,\"name\":\"NC-17\",\"description\":\"Лицам до 18 лет просмотр запрещен\"}]"))
                );
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(request(GET, "/mpa/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(
                        "{\"id\":1,\"name\":\"G\",\"description\":\"Нет возрастных ограничений\"}"))
                );
    }
}