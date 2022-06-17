package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    UserStorage userStorage;
    UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User addNewUser(@RequestBody @Valid User user) {
        user = usernameChecker(user);
        return userStorage.addUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User updateUser(@RequestBody(required = false) User user, @PathVariable(required = false) long id,
                              @PathVariable(required = false) long friendId) throws ValidationException {
        if (user != null) {
            user = usernameChecker(user);
            userIdChecker(user);
            return userStorage.updateUser(user);
        }
        return userService.addFriend(id, friendId);
    }


    @GetMapping({"/{id}/friends", "/{id}/friends/common/{otherId}", ""})
    public Set<User> getAllUser(@PathVariable(value = "id", required = false) Long id,
                                @PathVariable(value = "otherId", required = false) Long otherId){
        if (otherId != null && id != null) {
            return userService.getCommonFriends(id, otherId);
        }
        if (id != null) {
            return userService.getUserFriends(id);
        }
        return userStorage.getUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userStorage.getUserById(id);
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
