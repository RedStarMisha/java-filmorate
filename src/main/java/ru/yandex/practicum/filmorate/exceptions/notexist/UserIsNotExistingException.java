package ru.yandex.practicum.filmorate.exceptions.notexist;

public class UserIsNotExistingException extends EntityIsNotExistingException {

    public UserIsNotExistingException(String message) {
        super(message);
    }
}
