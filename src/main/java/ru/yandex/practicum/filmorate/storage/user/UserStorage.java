package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Set;

public interface UserStorage extends Storage<User> {

    User add(User user);

    User update(User user) throws UserIsNotExistingException;

    void delete(long id) throws UserIsNotExistingException;

    User getById(long id) throws UserIsNotExistingException;

    Set<User> getAll();
}
