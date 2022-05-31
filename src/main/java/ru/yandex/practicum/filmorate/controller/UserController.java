package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    @Override
    @PostMapping
    public User addNewElement(@RequestBody User element) throws ValidationException {
        element = userChecker(element);
        final User newUser = element.toBuilder()
                .id(setId())
                .build();
        dataMap.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    @PutMapping
    public User updateElement(User element) throws ValidationException {
        element = userChecker(element);
        userIdChecker(element);
        dataMap.put(element.getId(), element);
        return element;
    }
    private void userIdChecker(User user) throws ValidationException {
        if (user.getId() < 1) {
            throw new ValidationException("id пользователя должен быть больше 1");
        }
    }

    private User userChecker(User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Почта введена неверно");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин введена неверное");

        }
        if (user.getBirthday().isAfter(LocalDate.now()) ||
                user.getBirthday().isBefore(user.getBirthday().minusYears(100))) {
            throw new ValidationException("Не все могут рождаться в завтрашнем дне, а точнее никто");
        }
        if (user.getName().isBlank()) {
            return user.toBuilder()
                    .name(user.getLogin())
                    .build();
        }
        return user;
    }
}
