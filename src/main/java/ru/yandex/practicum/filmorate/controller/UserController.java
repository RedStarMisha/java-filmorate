package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    UserStorage userStorage;
    UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @PostMapping
    public User addNewUser(@RequestBody @Valid User user) {
        return userStorage.add(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) {
        return userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(required = false) Long id,
                           @PathVariable(required = false) Long friendId) {
        userService.addFriend(id, friendId);
    }

    @GetMapping({"/{id}/friends", "/{id}/friends/common/{otherId}", ""})
    public Set<User> getAllUser(@PathVariable(value = "id", required = false) Long id,
                                @PathVariable(value = "otherId", required = false) Long otherId){
        if (otherId == null && id == null) {
            return userStorage.getAll();
        }
        if (otherId == null) {
            return userService.getUserFriends(id);
        }
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userStorage.getById(id);
    }

    @DeleteMapping("/{id}/friends/{friendsId}")
    public void deleteFriend(@PathVariable("id") Long userId, @PathVariable("friendsId") Long friendsId) {
        userService.deleteFriend(userId, friendsId);
    }
}
