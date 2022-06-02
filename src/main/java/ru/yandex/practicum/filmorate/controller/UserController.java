package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    @PostMapping
    @Override
    public User addNewElement(@RequestBody @Valid User element) {
        //element = userChecker(element);
        try {
            final User newUser = element.toBuilder()
                    .id(setId())
                    .build();
            return super.addToMap(newUser.getId(), newUser);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return element;
    }

    @PutMapping
    @Override
    public User updateElement(@RequestBody User element) throws ValidationException {
        element = userChecker(element);
        userIdChecker(element);
        return super.addToMap(element.getId(), element);
    }

    private void userIdChecker(User user) throws ValidationException {
        if (user.getId() < 1) {
            throw new ValidationException("id пользователя должен быть больше 1");
        }
    }

    private User userChecker(User user) throws ValidationException {
//        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
//            throw new ValidationException("Почта введена неверно");
//        }
//        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
//            throw new ValidationException("Логин введена неверное");
//        }
//        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now()) ||
//                user.getBirthday().isBefore(user.getBirthday().minusYears(100))) {
//            throw new ValidationException("Не все могут рождаться в завтрашнем дне, а точнее никто");
//        }
        if (user.getName() == null || user.getName().isBlank()) {
            return user.toBuilder()
                    .name(user.getLogin())
                    .build();
        }
        return user;
    }
}
