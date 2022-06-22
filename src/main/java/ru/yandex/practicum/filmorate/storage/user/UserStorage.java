package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Set;

public interface UserStorage extends Storage<User> {

    User add(User user);

    User update(User user);

    void delete(long id);

    User getById(long id);

    Set<User> getAll();
}
