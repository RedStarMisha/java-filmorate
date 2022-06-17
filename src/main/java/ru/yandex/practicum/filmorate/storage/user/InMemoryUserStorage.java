package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ElementIsNotExisting;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public User addUser(User user) {
        user.setId(setId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен");
        return user;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public void deleteUser(long id) {
        userExistingChecker(id);
        users.remove(id);
    }

    @Override
    public User getUserById(long id) {
        userExistingChecker(id);
        return users.get(id);
    }

    @Override
    public Set<User> getUsers() {
        return new HashSet<>(users.values());
    }

    private long setId(){
        return id++;
    }

    private void userExistingChecker(long id) {
        if (!users.containsKey(id)) {
            throw new ElementIsNotExisting(String.format("Пользователя c id = %d не существует", id));
        }
    }
}
