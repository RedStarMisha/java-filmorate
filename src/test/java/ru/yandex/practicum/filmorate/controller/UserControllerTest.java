package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController controller;
    UserStorage userStorage;
    UserService userService;
    User u1;
    User u2;
    User u3;

    @BeforeEach
    void createController() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
        controller = new UserController(userStorage, new UserService(userStorage));
        u1 = User.builder()
                .name("Петя")
                .login("SexInstructor")
                .birthday(LocalDate.of(1990, 8, 13))
                .email("petyasexinstructor@yandex.ru")
                .build();
        u2 = User.builder()
                .name("Вася")
                .login("Danger")
                .birthday(LocalDate.of(1991, 8, 13))
                .email("danger@yandex.ru")
                .build();
        u3 = User.builder()
                .name("Коля")
                .login("Fear")
                .birthday(LocalDate.of(1989, 8, 13))
                .email("fear@yandex.ru")
                .build();
    }

    @Test
    void shouldAddNewUser() {
        controller.addNewUser(u1);
        assertAll(
                () -> assertEquals(1, controller.getAllUser(null, null).size()),
                () -> assertEquals(u1, controller.getUserById(1L))
        );
    }

    @Test
    void shouldUpdateUser() {
        controller.addNewUser(u1);
        u2.setId(1);
        controller.updateUser(u2);
        assertEquals(userStorage.getAll().size(), 1);
        assertEquals(userStorage.getById(1L), u2);
    }

    @Test
    void shouldAddFriend() {
        controller.addNewUser(u1);
        controller.addNewUser(u2);
        controller.addFriend(u1.getId(), u2.getId());
        assertEquals(u1.getFriendsId().size(), 1);
        assertTrue(u1.getFriendsId().contains(2L));
        assertTrue(u2.getFriendsId().contains(1L));
    }

    @Test
    void shouldThrowExceptionWhenAreFriendsWithUnknownId() {
        controller.addNewUser(u1);
        UserIsNotExistingException e = assertThrows(UserIsNotExistingException.class,
                () -> controller.addFriend(-1L, 1L));
        assertAll(
                () -> e,
                () ->
        );
    }

}