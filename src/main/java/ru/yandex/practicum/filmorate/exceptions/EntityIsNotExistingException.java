package ru.yandex.practicum.filmorate.exceptions;

public class EntityIsNotExistingException extends RuntimeException {

    public EntityIsNotExistingException(String message) {
        super(message);
    }
}
