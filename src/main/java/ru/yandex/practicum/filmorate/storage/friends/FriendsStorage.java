package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendsStorage {

    void add(long userId, long friendId) throws UserIsNotExistingException;

    Set<User> getFriends(long userId) throws UserIsNotExistingException;

    Set<User> getCommonFriends(long userId, long otherUserId) throws UserIsNotExistingException;

    void deleteFriend(long userId, long friendId) throws UserIsNotExistingException;
}
