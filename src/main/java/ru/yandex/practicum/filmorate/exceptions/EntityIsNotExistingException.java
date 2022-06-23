package ru.yandex.practicum.filmorate.exceptions;

public class EntityIsNotExistingException extends Exception {

    public EntityIsNotExistingException(String message) {
        super(message);
    }
}
