package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest extends UtilControllerTest<User> {
//    UserController controller;
//    UserStorage userStorage;
//    @BeforeEach
//    void createController() {
//        userStorage = new InMemoryUserStorage();
//        controller = new UserController(userStorage, new UserService(userStorage));
//        element = User.builder()
//                .id(0)
//                .name("Петя")
//                .login("sexinstructor")
//                .birthday(LocalDate.of(1990, 8, 13))
//                .email("petyasexinstructor@yandex.ru")
//                .build();
//    }
//
//    @Test
//    void shouldAddNewUser() throws ValidationException {
//        User addedUser = controller.addNewUser(element);
//        assertAll(
//                () -> assertEquals(1, controller.getAllUser().size()),
//                () -> assertEquals(1, addedUser.getId()),
//                () -> assertEquals(element.getName(), addedUser.getName()),
//                () -> assertEquals(element.getBirthday(), addedUser.getBirthday()),
//                () -> assertEquals(element.getLogin(), addedUser.getLogin()),
//                () -> assertEquals(element.getEmail(), addedUser.getEmail())
//        );
//    }
}