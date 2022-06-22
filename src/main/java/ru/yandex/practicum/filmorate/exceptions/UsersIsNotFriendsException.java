package ru.yandex.practicum.filmorate.exceptions;

public class UsersIsNotFriendsException extends RuntimeException{

    public UsersIsNotFriendsException(long userId, long friendId) {
        super("Пользователи с id " + userId + " и " + friendId + " не друзья");
    }
}
