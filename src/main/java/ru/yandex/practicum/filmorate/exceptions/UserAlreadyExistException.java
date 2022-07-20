package ru.yandex.practicum.filmorate.exceptions;

public class UserAlreadyExistException extends Exception {

    public UserAlreadyExistException(String login) {
        super(String.format("Пользователь с login %s уже существует", login));
    }
}
