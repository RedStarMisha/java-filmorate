package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.myvalidator.Date;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
    @PositiveOrZero
    @Max(Integer.MAX_VALUE)
    private final int id;
    @NotEmpty
    private final String name;
    @NotEmpty
    @Size(max = 200)
    private final String description;
    @Date
    private final LocalDate releaseDate;
    @Positive
    @Max(2400)
    private final long duration;
}