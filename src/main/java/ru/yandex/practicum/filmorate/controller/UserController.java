package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> userMap = new HashMap<>();
    private int id = 1;

    @PostMapping
    public User addNewUser(@RequestBody User user) throws ValidationException {
        user = userChecker(user);
        final User newUser = user.toBuilder()
                .id(setId())
                .build();
        userMap.put(newUser.getId(), newUser);
        return newUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) throws ValidationException {
        user = userChecker(user);
        userIdChecker(user);
        userMap.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    private int setId() {
        return id++;
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
        if (user.getBirthday().isAfter(LocalDate.now())) {
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
