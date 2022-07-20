package ru.yandex.practicum.filmorate.service.userservice;

import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;
public interface UserService {

    User addUser(User user) throws UserAlreadyExistException;

    User updateUser(User user) throws UserIsNotExistingException;

    void deleteUser(long id) throws UserIsNotExistingException;

    Set<User> getAllUsers();

    User getUserById(long id) throws UserIsNotExistingException;

    User addFriend(long userId, long friendId) throws UserIsNotExistingException;

    void deleteFriend(long userId, long friendId) throws UserIsNotExistingException;

    Set<User> getFriendsByUserId(long id) throws UserIsNotExistingException;

    Set<User> getCommonFriends(long userId, long otherId) throws UserIsNotExistingException;
}
