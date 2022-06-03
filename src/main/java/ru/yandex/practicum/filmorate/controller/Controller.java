package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class Controller<T> {
    private final Map<Integer, T> dataMap = new HashMap<>();
    private int id = 1;

    protected int setId() {
        return id++;
    }

    protected T addToMap(int id, T element){
        dataMap.put(id, element);
        log.info(element.toString());
        return element;
    }

    public abstract T updateElement(T t) throws ValidationException;

    public abstract T addNewElement(T t) throws ValidationException;

    @GetMapping
    public List<T> getAllElements() {
        return new ArrayList<>(dataMap.values());
    }
}
