package ru.yandex.practicum.filmorate.service.userservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.NoCommonFriendsException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    UserStorage userStorage;
    private final Function<Long, User> idToUserFunction = (ind) -> {
        try {
            return userStorage.getById(ind);
        } catch (UserIsNotExistingException e) {
            log.warn("user с id = {} не существует", ind);
        }
        return null;
    };

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) throws UserAlreadyExistException {
        user = usernameChecker(user);
        return userStorage.add(user);
    }

    public User updateUser(User user) throws UserIsNotExistingException {
        user = usernameChecker(user);
        return userStorage.update(user);
    }

    public void deleteUser(long id) throws UserIsNotExistingException {
        userStorage.delete(id);
    }

    public Set<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(long id) throws UserIsNotExistingException {
        return userStorage.getById(id);
    }

    public User addFriend(long userId, long friendId) throws UserIsNotExistingException {
        User user = getUserById(userId);
        user.addFriend(userId);
        user = userStorage.getById(userId);
        user.addFriend(friendId);
        return user;
    }

    public void deleteFriend(long userId, long friendId) throws UserIsNotExistingException {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
    }

    public Set<User> getFriendsByUserId(long id) throws UserIsNotExistingException {
        Set<Long> friends = userStorage.getById(id).getFriendsId();
        if (friends == null) {
            return null;
        }
        return friends.stream()
                    .map(idToUserFunction)
                    .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(long userId, long otherId) throws UserIsNotExistingException {
        Function<Long, User> idToUserFunction = (ind) -> {
            try {
                return userStorage.getById(ind);
            } catch (UserIsNotExistingException e) {
                e.printStackTrace();
            }
            return null;
        };
        Set<Long> userFriends = userStorage.getById(userId).getFriendsId();
        Set<Long> otherFriends = userStorage.getById(otherId).getFriendsId();
        if (userFriends == null || otherFriends == null) {
            throw new NoCommonFriendsException(userId, otherId);
        }
        return userFriends.stream()
                .filter(fr -> otherFriends.contains(fr))
                .map(idToUserFunction)
                .collect(Collectors.toSet());
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
