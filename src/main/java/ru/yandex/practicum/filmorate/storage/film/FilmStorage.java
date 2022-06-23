package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Set;

public interface FilmStorage extends Storage<Film> {

    Film add(Film film);

    Film update(Film film) throws FilmIsNotExistingException;

    void delete(long id) throws FilmIsNotExistingException;

    Set<Film> getAll();

    Film getById(long id) throws FilmIsNotExistingException;
}
