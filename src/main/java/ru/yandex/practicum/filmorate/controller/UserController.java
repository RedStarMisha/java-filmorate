package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addNewUser(@RequestBody @Valid User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody @Valid User user) throws UserIsNotExistingException {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable(required = false) Long id,
                           @PathVariable(required = false) Long friendId) throws UserIsNotExistingException {
        userService.addFriend(id, friendId);
    }

    @GetMapping
    public Set<User> getAllUser() {
        return userService.getAllUsers();
    }

    @GetMapping({"/{id}/friends", "/{id}/friends/common/{otherId}"})
    public Set<User> getFriends(@PathVariable(value = "id") Long id,
                                @PathVariable(value = "otherId", required = false) Long otherId) throws UserIsNotExistingException {
        if (otherId == null) {
            return userService.getUserFriends(id);
        }
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) throws UserIsNotExistingException {
        return userService.getUserById(id);
    }

    @DeleteMapping({"/{id}/friends/{friendsId}", "/{id}"})
    public void delete(@PathVariable("id") Long userId,
                       @PathVariable(value = "friendsId", required = false) Long friendsId) throws UserIsNotExistingException {
        if (friendsId == null) {
            userService.deleteUser(userId);
            return;
        }
        userService.deleteFriend(userId, friendsId);
    }
}
