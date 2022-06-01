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
    private final int id;
    @Email
    private final String email;
    @NotBlank
    @NotNull
    private final String login;
    @NotBlank
    @NotNull
    private final String name;
    @NotNull
    @PastOrPresent
    private final LocalDate birthday;
}
