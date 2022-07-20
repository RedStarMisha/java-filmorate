package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.notexist.FilmIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Long, Film> films = new HashMap<>();
    private long id = 1;

    @Override
    public void delete(long id) throws FilmIsNotExistingException {
        filmExistingChecker(id);
        log.info("Фильм " + films.get(id).getName() + " c id = " + id + " удален");
        films.remove(id);
    }

    @Override
    public Film add(Film film) {
        film.setId(setFilmId());
        films.put(film.getId(), film);
        log.info(String.format("Фильм %s c id = %d добавлен", film.getName(), film.getId()));
        return film;
    }

    @Override
    public Film update(Film film) throws FilmIsNotExistingException {
        filmExistingChecker(film.getId());
        films.put(film.getId(), film);
        log.info(String.format("Фильм %s c id = %d обновлен", film.getName(), film.getId()));
        return film;
    }

    @Override
    public Set<Film> getAll() {
        return new HashSet<>(films.values());
    }

    @Override
    public Film getById(long id) throws FilmIsNotExistingException {
        filmExistingChecker(id);
        return films.get(id);
    }


    private long setFilmId(){
        return id++;
    }

    private void filmExistingChecker(long id) throws FilmIsNotExistingException {
        if (!films.containsKey(id)) {
            throw new FilmIsNotExistingException(String.format("Фильма c id = %d не существует", id));
        }
    }
}
