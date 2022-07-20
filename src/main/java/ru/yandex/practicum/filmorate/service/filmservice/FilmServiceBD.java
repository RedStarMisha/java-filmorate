package ru.yandex.practicum.filmorate.service.filmservice;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.notexist.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.GenreIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.MPAIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmsrating.FilmsRatingStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Qualifier("BD")
@RequiredArgsConstructor //включает в себя @Autowired
public class FilmServiceBD implements FilmService {
    private final FilmStorage filmStorage;
    private final FilmsRatingStorage filmsRating;

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
        filmsRating.addLike(filmId, userId);
        return filmStorage.getById(filmId);
    }

    public void deleteLikeFromFilm(long filmId, long userId) throws FilmIsNotExistingException, UserIsNotExistingException {
        filmsRating.deleteLikeFromFilm(filmId, userId);
    }

    public List<Film> getPopularFilms(long count) {
        return filmsRating.getPopularFilms(count);
    }
}
