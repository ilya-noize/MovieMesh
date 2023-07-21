package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest()
public class UserControllerTest {
    private static final LocalDate RIGHT_BIRTHDAY = LocalDate.of(2000, 1, 1);
    private static final LocalDate WRONG_BIRTHDAY = LocalDate.now();

    UserController controller = new UserController(
            new UserServiceImpl(
                    new InMemoryUserStorage()
            ));

    @DisplayName(value = "Создать пользователя")
    @Test
    void createUser() {
        User user = controller.create(getUser());
        assertNotNull(user.getId());
    }

    private User getUser() {
        return new User("login", "name", "login@yahoo.com", RIGHT_BIRTHDAY);
    }

    @DisplayName(value = "Создать пользователя - Безымянный: Логин = Имя")
    @Test
    void createUserNoName() {
        User user = controller.create(getUserNoName());
        assertEquals(user.getLogin(), user.getName());
    }

    private User getUserNoName() {
        return new User("login", null, "login@yahoo.com", RIGHT_BIRTHDAY);
    }

    @DisplayName(value = "Создать пользователя - Ошибка:Неверный email")
    @Test
    void createUserFailEmail() {

        try {
            controller.create(getUserFailEmail());
        } catch (Exception e) {
            assertEquals("Введенное значение не является адресом электронной почты.", e.getMessage());
        }
    }

    private User getUserFailEmail() {
        return new User("login", "name", "loginyahoocom@.", RIGHT_BIRTHDAY);
    }

    @DisplayName(value = "Создать пользователя - Ошибка:Пробел в логине")
    @Test
    void createUserFailSpaceLogin() {
        try {
            controller.create(getUserFailSpaceLogin());
        } catch (Exception e) {
            assertEquals("Логин не может содержать пробелы.", e.getMessage());
        }
    }

    private User getUserFailSpaceLogin() {
        return new User("log in", "name", "login@yahoo.com", RIGHT_BIRTHDAY);
    }

    @DisplayName(value = "Создать пользователя - Ошибка:Короткий логин")
    @Test
    void createUserFailShortLogin() {
        try {
            controller.create(getUserFailShortLogin());
        } catch (Exception e) {
            assertEquals("Логин должен быть от 3 до 20 символов.", e.getMessage());
        }
    }

    private User getUserFailShortLogin() {
        return new User("l", "name", "login@yahoo.com", RIGHT_BIRTHDAY);
    }

    @DisplayName(value = "Создать пользователя - Ошибка:Длинный логин")
    @Test
    void createUserFailLongLogin() {
        try {
            controller.create(getUserFailLongLogin());
        } catch (Exception e) {
            assertEquals("Логин должен быть от 3 до 20 символов.", e.getMessage());
        }
    }

    private User getUserFailLongLogin() {
        return new User("loginIsMoreTwentySymbols", "name", "login@yahoo.com", RIGHT_BIRTHDAY);
    }

    @DisplayName(value = "Создать пользователя - Ошибка:Дата рождения не в прошлом")
    @Test
    void createUserFailWrongBirthday() {
        try {
            controller.create(getUserFailBirthday());
        } catch (Exception e) {
            assertEquals("Дата рождения должна быть только в прошлом.", e.getMessage());
        }
    }

    private User getUserFailBirthday() {
        return new User("login", "name", "login@yahoo.com", WRONG_BIRTHDAY);
    }

    @DisplayName(value = "Изменить пользователя")
    @Test
    void updateUser() {
        User user = getUser();
        controller.create(user);
        user.setName("login2");
        assertEquals(1, controller.update(user).getId());
    }

    @DisplayName(value = "Изменить пользователя - Ошибка: несуществующий пользователь")
    @Test
    void updateUserInvalidUser() {
        User user = getUserNotExist();
        try {
            controller.update(user);
        } catch (Exception e) {
            assertEquals("Пользователь с уин:9999 не зарегистрирован.", e.getMessage());
        }
    }

    private User getUserNotExist() {
        User user = new User("qwerty", "Ivan", "login@yahoo.com", RIGHT_BIRTHDAY);
        user.setId(9999);
        return user;
    }

    @DisplayName(value = "Получить список пользователей")
    @Test
    void getUsers() {
        controller.create(getUser());
        int countUsers = controller.getAll().size();
        assertEquals(1, countUsers);
    }
}