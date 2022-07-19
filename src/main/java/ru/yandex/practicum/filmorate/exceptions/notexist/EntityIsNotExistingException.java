package ru.yandex.practicum.filmorate.exceptions.notexist;

public class EntityIsNotExistingException extends Exception {

    public EntityIsNotExistingException(String message) {
        super(message);
    }
}
