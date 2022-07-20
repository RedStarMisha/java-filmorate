package ru.yandex.practicum.filmorate.storage.filmsrating;

import ru.yandex.practicum.filmorate.exceptions.notexist.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmsRatingStorage {

    void addLike(long filmId, long userId) throws UserIsNotExistingException, FilmIsNotExistingException;


    void deleteLikeFromFilm(long filmId, long userId) throws FilmIsNotExistingException, UserIsNotExistingException;


    List<Film> getPopularFilms(long count);
}
