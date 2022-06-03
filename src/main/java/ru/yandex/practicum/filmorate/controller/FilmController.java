package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController()
@RequestMapping("/films")

public class FilmController extends Controller<Film> {


    @PutMapping
    @Override
    public Film updateElement(@RequestBody Film element) throws ValidationException {
        filmIdChecker(element);
        int elementId = element.getId();
        return super.addToMap(elementId, element);
    }

    @Override
    @PostMapping
    public Film addNewElement(@RequestBody @Valid Film film) {
        int elementId = setId();
        final Film newFilm = film.toBuilder()
                .id(elementId)
                .build();
        return super.addToMap(elementId, newFilm);
    }

    private void filmIdChecker(Film film) throws ValidationException {
        if (film.getId() < 1) {
            throw new ValidationException("id не может быть меньше 1");
        }
    }
}
