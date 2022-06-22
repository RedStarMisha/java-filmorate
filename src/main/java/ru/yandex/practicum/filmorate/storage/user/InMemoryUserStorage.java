package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    @Override
    public User add(User user) {
        user = usernameChecker(user);
        user.setId(setUserId());
        users.put(user.getId(), user);
        log.info(String.format("Пользователь %s с id = %d добавлен", user.getLogin(), user.getId()));
        return user;
    }

    @Override
    public User update(User user) {
        userExistingChecker(user.getId());
        user = usernameChecker(user);
        users.put(user.getId(), user);
        log.info(String.format("Пользователь %s с id = %d обновлен", user.getLogin(), user.getId()));
        return user;
    }

    @Override
    public void delete(long id) {
        userExistingChecker(id);
        users.remove(id);
        log.info(String.format("Пользователь с id = %d удален", id));
    }

    @Override
    public User getById(long id)  {
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

    public void userExistingChecker(long id) {
        if (!users.containsKey(id)) {
            throw new UserIsNotExistingException(String.format("Пользователя c id = %d не существует", id));
        }
    }

    private User usernameChecker(User user) {
        if (!StringUtils.hasText(user.getName())) {
            user = user.toBuilder()
                    .name(user.getLogin())
                    .build();
        }
        return user;
    }
}
