package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
@RequiredArgsConstructor
public class UserControllerTest {
    /*
    private static final LocalDate RIGHT_BIRTHDAY = LocalDate.of(2000, 1, 1);
    private static final LocalDate WRONG_BIRTHDAY = LocalDate.now();
    private UserController controller;

    @DisplayName(value = "Создать пользователя")
    @Test
    void createUser() {
        try {
            User user = controller.create(getUser());
            assertNotNull(user.getId());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    private User getUser() {
        return new User("login", "name", "login@yahoo.com", RIGHT_BIRTHDAY);
    }

    @DisplayName(value = "Создать пользователя - Безымянный: Логин = Имя")
    @Test
    void createUserNoName() {
        try {
            controller.create(getUserNoName());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
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
            assertNull(e.getMessage());
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
            assertNull(e.getMessage());
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
            assertNull(e.getMessage());
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
            assertNull(e.getMessage());
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
            assertNull(e.getMessage());
        }
    }

    private User getUserFailBirthday() {
        return new User("login", "name", "login@yahoo.com", WRONG_BIRTHDAY);
    }

    @DisplayName(value = "Изменить пользователя")
    @Test
    void updateUser() {
        try {
            User user = controller.create(getUser());
            user.setName("login2");
            user = controller.update(user);
            assertEquals(1L, user.getId());
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @DisplayName(value = "Изменить пользователя - Ошибка: несуществующий пользователь")
    @Test
    void updateUserInvalidUser() {
        User user = getUserNotExist();
        try {
            controller.update(user);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    private User getUserNotExist() {
        User user = new User("qwerty", "Ivan", "login@yahoo.com", RIGHT_BIRTHDAY);
        user.setId(9999L);
        return user;
    }

    @DisplayName(value = "Получить список пользователей")
    @Test
    void getUsers() {
        try {
            controller.create(getUser());
            int countUsers = controller.getAll().size();
            assertEquals(1, countUsers);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @DisplayName(value = "Добавить друга")
    @Test
    void addFriend() {
        try {
            controller.addFriend(null, null);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @DisplayName(value = "Удалить друга")
    @Test
    void deleteFriend() {
        try {
            controller.deleteFriend(null, null);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @DisplayName(value = "Получить список друзей")
    @Test
    void getUserFriends() {
        try {
            controller.getUserFriends(9999L);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

    @DisplayName(value = "Получить список общих друзей двух пользователей")
    @Test
    void getUserCommonFriends() {
        try {
            controller.getUserCommonFriends(null, null);
        } catch (Exception e) {
            assertNull(e.getMessage());
        }
    }

     */
}