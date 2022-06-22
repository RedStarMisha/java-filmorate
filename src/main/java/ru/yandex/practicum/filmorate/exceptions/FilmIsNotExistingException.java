package ru.yandex.practicum.filmorate.exceptions;

public class FilmIsNotExistingException extends EntityIsNotExistingException{

    public FilmIsNotExistingException(String message) {
        super(message);
    }
}
