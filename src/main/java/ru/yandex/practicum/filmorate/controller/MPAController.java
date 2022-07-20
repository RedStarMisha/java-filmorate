package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.notexist.MPAIsNotExistingException;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
public class MPAController {
    private final MpaService mpaService;

    @Autowired
    public MPAController(MpaService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping("/mpa/{id}")
    public Rating getById(@PathVariable(name = "id") Integer id) throws MPAIsNotExistingException {
            return mpaService.getMpaById(id);
    }

    @GetMapping("/mpa")
    public List<Rating> getAll() {
        return mpaService.getAll();
    }
}
