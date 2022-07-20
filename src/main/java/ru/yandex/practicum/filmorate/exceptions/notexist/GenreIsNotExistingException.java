package ru.yandex.practicum.filmorate.exceptions.notexist;

public class GenreIsNotExistingException extends EntityIsNotExistingException{

    public GenreIsNotExistingException(String message) {
        super(message);
    }
}
