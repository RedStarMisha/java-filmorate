package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ElementIsNotExisting;
import ru.yandex.practicum.filmorate.exceptions.NoCommonFriendsException;
import ru.yandex.practicum.filmorate.exceptions.UsersIsNotFriendsException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    InMemoryUserStorage userStorage;

    @Autowired
    public UserService(InMemoryUserStorage userStorage){
        this.userStorage = userStorage;
    }

    public User addFriend(long userId, long friendId) throws ElementIsNotExisting {
        User user = userStorage.getUserById(friendId);
        user.addFriend(userId);
        user = userStorage.getUserById(userId);
        user.addFriend(friendId);
        return user;
    }

    public void deleteFriend(long userId, long friendId) throws ElementIsNotExisting {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        if (!user.getFriendsId().contains(friendId)) {
            throw new UsersIsNotFriendsException();
        }
        user.getFriendsId().remove(friendId);
        friend.getFriendsId().remove(userId);
    }

    public Set<User> getUserFriends(long id) {
        Set<Long> friends = userStorage.getUserById(id).getFriendsId();
        if (friends == null) {
            return null;
        }
        return friends.stream()
                .map(friendsId -> userStorage.getUserById(friendsId))
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(long userId, long otherId) {
        Set<Long> userFriends = userStorage.getUserById(userId).getFriendsId();
        Set<Long> otherFriends = userStorage.getUserById(otherId).getFriendsId();
        if (userFriends == null || otherFriends == null) {
            throw new NoCommonFriendsException();
        }
        Set<User> commonFriends = userFriends.stream()
                .filter(fr -> otherFriends.contains(fr))
                .map(id -> userStorage.getUserById(id))
                .collect(Collectors.toSet());
        if (!commonFriends.isEmpty()) {
            return commonFriends;
        } else {
            throw new NoCommonFriendsException();
        }
    }
}
