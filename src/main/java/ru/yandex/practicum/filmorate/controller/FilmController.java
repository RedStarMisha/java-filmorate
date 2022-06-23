package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addNewFilm(@RequestBody @Valid Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) throws FilmIsNotExistingException {
            return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable(value = "id") Long filmId,
                              @PathVariable(value = "userId") Long userId) throws FilmIsNotExistingException, UserIsNotExistingException {
        filmService.addLikeToFilm(filmId, userId);
    }

    @DeleteMapping({"{id}", "/{id}/like/{userId}"})
    public void deleteFilmData(@PathVariable(value = "id") Long filmId,
                               @PathVariable(value = "userId", required = false) Long userId) throws FilmIsNotExistingException, UserIsNotExistingException {
        if (userId == null) {
            filmService.deleteFilm(filmId);
            return;
        }
        filmService.deleteLikeFromFilm(filmId, userId);
    }

    @GetMapping
    public Set<Film> getAllFilms() {
            return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(name = "id") Long filmId) throws FilmIsNotExistingException {
        return filmService.getFilmById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(
            @RequestParam(required = false, defaultValue = "10", name = "count") Long count) {
        return filmService.getPopularFilms(count);
    }
}
