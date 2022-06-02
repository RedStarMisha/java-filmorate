package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class User {
    @PositiveOrZero
    @Max(Integer.MAX_VALUE)
    private final int id;
    @Email
    private final String email;
    @NotEmpty
    @Pattern(message = "Не может содержать пробел",regexp = "\\w+")
    private final String login;
    private final String name;
    @NotNull
    @Past
    private final LocalDate birthday;
}
