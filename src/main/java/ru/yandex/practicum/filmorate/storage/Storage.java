package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.notexist.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.GenreIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.MPAIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;

import java.util.Set;

public interface Storage <T>{

    T add(T t) throws MPAIsNotExistingException, GenreIsNotExistingException, UserAlreadyExistException;

    T update(T t) throws UserIsNotExistingException, FilmIsNotExistingException, GenreIsNotExistingException;

    void delete(long id) throws UserIsNotExistingException, FilmIsNotExistingException;

    Set<T> getAll();

    T getById(long id) throws UserIsNotExistingException, FilmIsNotExistingException;
}
