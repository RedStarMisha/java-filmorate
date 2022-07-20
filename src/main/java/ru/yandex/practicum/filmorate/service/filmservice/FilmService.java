package ru.yandex.practicum.filmorate.service.filmservice;

import ru.yandex.practicum.filmorate.exceptions.notexist.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.GenreIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.MPAIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmService {

    Film addFilm(Film film) throws MPAIsNotExistingException, GenreIsNotExistingException;

    Film updateFilm(Film film) throws FilmIsNotExistingException, GenreIsNotExistingException;

    void deleteFilm(long id) throws FilmIsNotExistingException;

    Set<Film> getAllFilms();

    Film getFilmById(long id) throws FilmIsNotExistingException;

    Film addLikeToFilm(long filmId, long userId) throws UserIsNotExistingException, FilmIsNotExistingException;

    void deleteLikeFromFilm(long filmId, long userId) throws FilmIsNotExistingException, UserIsNotExistingException;

    List<Film> getPopularFilms(long count);
}
