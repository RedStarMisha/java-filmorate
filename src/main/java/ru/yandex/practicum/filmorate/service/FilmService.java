package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film addLikeToFilm(long filmId, long userId) {
        Film film = filmStorage.getById(filmId);
        userStorage.getById(userId);
        film.addLike(userId);
        return film;
    }

    public void deleteLikeFromFilm(long filmId, long userId) {
        Film film = filmStorage.getById(filmId);
        userStorage.getById(userId);
        film.deleteLike(userId);
    }

    public List<Film> getPopularFilms(long count) {
        Comparator<Film> filmComparator = Comparator.comparing(film -> film.getIdUserWhoLikedSet().size());
        filmComparator = filmComparator.thenComparing(Film::getId);
        return filmStorage.getAll().stream()
                .sorted(filmComparator.reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
