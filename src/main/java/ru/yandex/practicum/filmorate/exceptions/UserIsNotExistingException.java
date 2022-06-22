package ru.yandex.practicum.filmorate.exceptions;

public class UserIsNotExistingException extends EntityIsNotExistingException {

    public UserIsNotExistingException(String message) {
        super(message);
    }
}
