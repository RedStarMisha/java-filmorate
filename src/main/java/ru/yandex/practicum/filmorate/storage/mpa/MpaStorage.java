package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.exceptions.notexist.MPAIsNotExistingException;
import ru.yandex.practicum.filmorate.storage.mpa.model.Rating;

import java.util.List;

public interface MpaStorage {

    Rating getMpaById(Integer id) throws MPAIsNotExistingException;

    List<Rating> getAll();
}
