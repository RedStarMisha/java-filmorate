package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoCommonFriendsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public User addFriend(long userId, long friendId){
        User user = userStorage.getById(friendId);
        user.addFriend(userId);
        user = userStorage.getById(userId);
        user.addFriend(friendId);
        return user;
    }

    public void deleteFriend(long userId, long friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
    }

    public Set<User> getUserFriends(long id) {
        Set<Long> friends = userStorage.getById(id).getFriendsId();
        if (friends == null) {
            return null;
        }
        return friends.stream()
                .map(friendsId -> userStorage.getById(friendsId))
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(long userId, long otherId) {
        Set<Long> userFriends = userStorage.getById(userId).getFriendsId();
        Set<Long> otherFriends = userStorage.getById(otherId).getFriendsId();
        if (userFriends == null || otherFriends == null) {
            throw new NoCommonFriendsException(userId, otherId);
        }
        return userFriends.stream()
                .filter(fr -> otherFriends.contains(fr))
                .map(id -> userStorage.getById(id))
                .collect(Collectors.toSet());
    }
}
