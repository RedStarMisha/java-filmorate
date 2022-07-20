package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.notexist.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.GenreIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.MPAIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Set;

public interface FilmStorage extends Storage<Film> {

    Film add(Film film) throws MPAIsNotExistingException, GenreIsNotExistingException;

    Film update(Film film) throws FilmIsNotExistingException, GenreIsNotExistingException;

    void delete(long id) throws FilmIsNotExistingException;

    Set<Film> getAll();

    Film getById(long id) throws FilmIsNotExistingException;
}
