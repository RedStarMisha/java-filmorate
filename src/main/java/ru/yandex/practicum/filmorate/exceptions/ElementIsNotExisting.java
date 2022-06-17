package ru.yandex.practicum.filmorate.exceptions;

public class ElementIsNotExisting extends RuntimeException{

    public ElementIsNotExisting(String message) {
        super(message);
    }
}
