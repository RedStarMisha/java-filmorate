package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest extends UtilControllerTest<User> {

    @BeforeEach
    void createController() {
        controller = new UserController();
        element = User.builder()
                .id(0)
                .name("Петя")
                .login("sexinstructor")
                .birthday(LocalDate.of(1990, 8, 13))
                .email("petyasexinstructor@yandex.ru")
                .build();
    }

    @Test
    void shouldAddNewUser() throws ValidationException {
        User addedUser = controller.addNewElement(element);
        assertAll(
                () -> assertEquals(1, controller.getAllElements().size()),
                () -> assertEquals(1, addedUser.getId()),
                () -> assertEquals(element.getName(), addedUser.getName()),
                () -> assertEquals(element.getBirthday(), addedUser.getBirthday()),
                () -> assertEquals(element.getLogin(), addedUser.getLogin()),
                () -> assertEquals(element.getEmail(), addedUser.getEmail())
        );
    }
}