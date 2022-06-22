package ru.yandex.practicum.filmorate.exceptions;

public class NoCommonFriendsException extends RuntimeException{
    public NoCommonFriendsException(long userId, long otherId) {
        super(String.format("У пользователей с id %d и %d нет общих друзей", userId, otherId));
    }
}
