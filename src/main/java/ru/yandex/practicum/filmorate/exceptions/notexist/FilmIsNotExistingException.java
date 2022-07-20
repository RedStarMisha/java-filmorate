package ru.yandex.practicum.filmorate.exceptions.notexist;

public class FilmIsNotExistingException extends EntityIsNotExistingException{

    public FilmIsNotExistingException(String message) {
        super(message);
    }
}
