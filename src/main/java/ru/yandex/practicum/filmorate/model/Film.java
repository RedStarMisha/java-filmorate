package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
    @PositiveOrZero
    @Max(Integer.MAX_VALUE)
    private final int id;
    @NotBlank
    @NotNull
    private final String name;
    @NotNull
    private final String description;
    @PastOrPresent
    private final LocalDate releaseDate;
    @Positive
    @Max(2400)
    private final long duration;
}
