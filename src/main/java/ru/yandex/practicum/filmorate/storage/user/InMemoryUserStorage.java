package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public User add(User user) {
        user.setId(setUserId());
        users.put(user.getId(), user);
        log.info(String.format("Пользователь %s с id = %d добавлен", user.getLogin(), user.getId()));
        return user;
    }

    @Override
    public User update(User user) throws UserIsNotExistingException {
        userExistingChecker(user.getId());
        users.put(user.getId(), user);
        log.info(String.format("Пользователь %s с id = %d обновлен", user.getLogin(), user.getId()));
        return user;
    }

    @Override
    public void delete(long id) throws UserIsNotExistingException {
        userExistingChecker(id);
        users.remove(id);
        log.info(String.format("Пользователь с id = %d удален", id));
    }

    @Override
    public User getById(long id) throws UserIsNotExistingException {
        userExistingChecker(id);
        return users.get(id);
    }

    @Override
    public Set<User> getAll() {
        return new HashSet<>(users.values());
    }

    private long setUserId(){
        return id++;
    }

    public void userExistingChecker(long id) throws UserIsNotExistingException {
        if (!users.containsKey(id)) {
            throw new UserIsNotExistingException(String.format("Пользователя c id = %d не существует", id));
        }
    }
}
