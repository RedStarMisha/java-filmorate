package ru.yandex.practicum.filmorate.service.filmservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.notexist.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.notexist.GenreIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.MPAIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmServiceImpl(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) throws MPAIsNotExistingException, GenreIsNotExistingException {
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) throws FilmIsNotExistingException, GenreIsNotExistingException {
        return filmStorage.update(film);
    }

    public void deleteFilm(long id) throws FilmIsNotExistingException {
        filmStorage.delete(id);
    }

    public Set<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(long id) throws FilmIsNotExistingException {
        return filmStorage.getById(id);
    }

    public Film addLikeToFilm(long filmId, long userId) throws UserIsNotExistingException, FilmIsNotExistingException {
        Film film = filmStorage.getById(filmId);
        userStorage.getById(userId);
        film.addLike(userId);
        return film;
    }

    public void deleteLikeFromFilm(long filmId, long userId) throws FilmIsNotExistingException, UserIsNotExistingException {
        Film film = filmStorage.getById(filmId);
        userStorage.getById(userId);
        film.deleteLike(userId);
    }

    public List<Film> getPopularFilms(long count) {
        if (count < 1) {
            throw new IncorrectParameterException("count");
        }
        Comparator<Film> filmComparator = Comparator.comparing(film -> film.getIdUserWhoLikedSet().size());
        filmComparator = filmComparator.thenComparing(Film::getId);
        return filmStorage.getAll().stream()
                .sorted(filmComparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
