package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.UserIsNotExistingException;

import java.util.Set;

public interface Storage <T>{

    T add(T t);

    T update(T t) throws UserIsNotExistingException, FilmIsNotExistingException;

    void delete(long id) throws UserIsNotExistingException, FilmIsNotExistingException;

    Set<T> getAll();

    T getById(long id) throws UserIsNotExistingException, FilmIsNotExistingException;
}
