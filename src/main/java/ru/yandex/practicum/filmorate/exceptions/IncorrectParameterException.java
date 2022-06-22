package ru.yandex.practicum.filmorate.exceptions;

import lombok.Data;

@Data
public class IncorrectParameterException extends RuntimeException{
    private final String parameter;
}
