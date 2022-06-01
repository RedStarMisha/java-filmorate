package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@RestController()
@RequestMapping("/films")

public class FilmController extends Controller<Film> {


    @PutMapping
    @Override
    public Film updateElement(@RequestBody Film element) throws ValidationException {
        element = filmChecker(element);
        filmIdChecker(element);
        int elementId = element.getId();
        return super.addToMap(elementId, element);
    }

    @Override
    @PostMapping
    public Film addNewElement(@RequestBody Film film) throws ValidationException {
        int elementId = setId();
        film = filmChecker(film);
        final Film newFilm = film.toBuilder()
                .id(elementId)
                .build();
        return super.addToMap(elementId, newFilm);
    }

    private Film filmChecker(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDuration() <= 0 || film.getDuration() > 2400) {
            throw new ValidationException("Недопустимая длительность");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isAfter(LocalDate.now())
                || film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("Недопустимая дата релиза");
        }
        if (film.getDescription() == null || film.getDescription().trim().length() > 200) {
            throw new ValidationException("Не допустимое описание");
        }
        return film.toBuilder()
                .description(film.getDescription().trim())
                .build();
    }

    private void filmIdChecker(Film film) throws ValidationException {
        if (film.getId() < 1) {
            throw new ValidationException("id не может быть меньше 1");
        }
    }
}
