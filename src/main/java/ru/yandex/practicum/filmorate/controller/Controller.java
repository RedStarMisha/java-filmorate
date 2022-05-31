package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Controller<T> {
    protected final Map<Integer, T> dataMap = new HashMap<>();
    private int id = 1;

    protected int setId() {
        return id++;
    }

    public abstract T addNewElement(@RequestBody T element) throws ValidationException;

    public abstract T updateElement(@RequestBody T element) throws ValidationException;

    @GetMapping
    public List<T> getAllFilm() {
        return new ArrayList<>(dataMap.values());
    }
}
