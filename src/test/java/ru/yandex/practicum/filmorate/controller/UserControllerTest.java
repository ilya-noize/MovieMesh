package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    private static MockHttpServletRequestBuilder requestBuilder;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        requestBuilder = null;
    }

    @Test
    void create() throws Exception {
        Mockito.when(userService.create(getUser())).thenReturn(getUser());

        requestBuilder = post("/users")
                .param("\"login\"", "\"dolore\"")
                .param("\"name\"", "\"Nick Name\"")
                .param("\"email\"", "\"mail@mail.ru\"")
                .param("\"birthday\"", "\"1946-08-20\"");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update() throws Exception {
        Mockito.when(userService.update(getUser())).thenReturn(getUser());

        create();
        requestBuilder = post("/users")
                .param("\"login\"", "\"doloreUpdate\"")
                .param("\"name\"", "\"est adipisicing\"")
                .param("\"id\"", "\"1\"")
                .param("\"email\"", "\"mail@yandex.ru\"")
                .param("\"birthday\"", "\"1976-09-20\"");
        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void get() throws Exception {
        mockMvc.perform(request(GET, "/users/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void createFriend() throws Exception {
        mockMvc.perform(request(PUT, "/users/1/friends/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteFriend() throws Exception {
        mockMvc.perform(request(DELETE, "/users/1/friends/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void getFriends() throws Exception {
        mockMvc.perform(request(GET, "/users/1/friends"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void getCommonFriends() throws Exception {
        mockMvc.perform(request(GET, "/users/1/friends/common/2"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void getAll() throws Exception {
        mockMvc.perform(request(GET, "/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("[]")));
    }

    private User getUser() {
        return new User(
                1L,
                "email@mail.ru",
                "login",
                "name",
                LocalDate.of(2000, 1, 1)
        );
    }
}