package ru.yandex.practicum.filmorate.model;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
    private final int id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final long duration;
}
