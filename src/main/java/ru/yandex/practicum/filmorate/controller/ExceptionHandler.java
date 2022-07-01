package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.*;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({UserIsNotExistingException.class, FilmIsNotExistingException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse entityIsNotExisting(Exception e) {
        return new ExceptionResponse(e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IncorrectParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse incorrectParameter(IncorrectParameterException e) {
        return new ExceptionResponse("Некорректно введен параметр " + e.getParameter());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({NoCommonFriendsException.class, UsersIsNotFriendsException.class})
    public ExceptionResponse friendsError(RuntimeException e) {
        return new ExceptionResponse(e.getMessage());
    }
}
