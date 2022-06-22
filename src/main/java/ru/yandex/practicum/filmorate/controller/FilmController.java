package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
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
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping
    public Film addNewFilm(@RequestBody @Valid Film film) {
        return filmStorage.add(film);
    }

    @PutMapping()
    public Film updateFilm(@RequestBody Film film) {
            return filmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeToFilm(@PathVariable(value = "id") Long filmId,
                              @PathVariable(value = "userId") Long userId) {
        filmService.addLikeToFilm(filmId, userId);
    }

    @DeleteMapping({"{id}", "/{id}/like/{userId}"})
    public void deleteFilmData(@PathVariable(value = "id") Long filmId,
                               @PathVariable(value = "userId", required = false) Long userId) {
        if (userId == null) {
            filmStorage.delete(filmId);
            return;
        }
        filmService.deleteLikeFromFilm(filmId, userId);
    }

    @GetMapping
    public Set<Film> getAllFilms() {
            return filmStorage.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable(name = "id") Long filmId) {
        return filmStorage.getById(filmId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(
            @RequestParam(required = false, defaultValue = "10", name = "count") Long count) {
        if (count < 1) {
            throw new IncorrectParameterException("count");
        }
        return filmService.getPopularFilms(count);
    }
}
