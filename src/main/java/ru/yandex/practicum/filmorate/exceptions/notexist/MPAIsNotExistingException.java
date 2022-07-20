package ru.yandex.practicum.filmorate.exceptions.notexist;

public class MPAIsNotExistingException extends EntityIsNotExistingException{

    public MPAIsNotExistingException(String message) {
        super(message);
    }
}
