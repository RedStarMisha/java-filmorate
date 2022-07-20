package ru.yandex.practicum.filmorate.service.userservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendsStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;
import java.util.function.Function;

@Service
@Qualifier("BD")
@Slf4j
public class UserServiceBD implements UserService {
    UserStorage userStorage;
    private final FriendsStorage friendsStorage;

    @Autowired
    public UserServiceBD(UserStorage userStorage, FriendsStorage friendsStorage){
        this.userStorage = userStorage;
        this.friendsStorage = friendsStorage;
    }


    public User addUser(User user) throws UserAlreadyExistException {
        return userStorage.add(user);
    }

    public User updateUser(User user) throws UserIsNotExistingException {
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
        friendsStorage.add(userId, friendId);
        return userStorage.getById(friendId);
    }

    public void deleteFriend(long userId, long friendId) throws UserIsNotExistingException {
        friendsStorage.deleteFriend(userId, friendId);
    }

    public Set<User> getFriendsByUserId(long userId) throws UserIsNotExistingException {
        return friendsStorage.getFriends(userId);
    }

    public Set<User> getCommonFriends(long userId, long otherId) throws UserIsNotExistingException {
        return friendsStorage.getCommonFriends(userId, otherId);
    }
}
