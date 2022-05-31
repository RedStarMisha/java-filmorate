package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@RestController()
@RequestMapping("/films")
@Slf4j
public class FilmController extends Controller<Film> {

    @Override
    @PostMapping
    public Film addNewElement(@RequestBody Film element) throws ValidationException {
        System.out.println(element);
        element = filmChecker(element);
        final Film newFilm = element.toBuilder()
                .id(setId())
                .build();
        dataMap.put(newFilm.getId(), newFilm);
        log.info(newFilm.toString());
        return newFilm;
    }

    @Override
    @PutMapping
    public Film updateElement(@RequestBody Film element) throws ValidationException {
        element = filmChecker(element);
        filmIdChecker(element);
        dataMap.put(element.getId(), element);
        log.info(element.toString());
        return element;
    }

    private Film filmChecker(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDuration().toMinutes() <= 0) {
            throw new ValidationException("Недопустимая длительность");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("Недопустимая дата релиза");
        }
        if (film.getDescription().trim().length() > 200) {
            log.info("Длина описание больше 200. Лишнее обрезано");
            Film xx = film.toBuilder()
                    .description(film.getDescription().trim().substring(0 , 200))
                    .build();
            return xx;
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
