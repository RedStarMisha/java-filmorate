package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.exceptions.notexist.GenreIsNotExistingException;
import ru.yandex.practicum.filmorate.storage.mpa.model.Genre;

import java.util.List;

public interface GenreStorage {

    Genre getById(int id) throws GenreIsNotExistingException;

    List<Genre> getAll();
}
