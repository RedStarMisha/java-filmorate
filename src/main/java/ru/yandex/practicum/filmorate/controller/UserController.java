package ru.yandex.practicum.filmorate.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {

    @PostMapping
    @Override
    public User addNewElement(@RequestBody @Valid User element) {
        element = usernameChecker(element);
        final User newUser = element.toBuilder()
                .id(setId())
                .build();
        return super.addToMap(newUser.getId(), newUser);
    }

    @PutMapping
    @Override
    public User updateElement(@RequestBody User element) throws ValidationException {
        element = usernameChecker(element);
        userIdChecker(element);
        return super.addToMap(element.getId(), element);
    }

    private void userIdChecker(User user) throws ValidationException {
        if (user.getId() < 1) {
            throw new ValidationException("id пользователя должен быть больше 1");
        }
    }

    private User usernameChecker(User user) {
        if (!StringUtils.hasText(user.getName())) {
            return user.toBuilder()
                    .name(user.getLogin())
                    .build();
        }
        return user;
    }
}
