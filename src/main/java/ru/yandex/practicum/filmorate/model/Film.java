package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.yandex.practicum.filmorate.exceptions.notexist.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.myvalidator.Date;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Film {
    @Getter
    @Setter
    @Max(Integer.MAX_VALUE)
    private long id;

    @NotEmpty
    final String name;

    @NotEmpty
    @Size(max = 200)
    private String description;

    @Date
    private LocalDate releaseDate;

    @Positive
    @Max(2400)
    private long duration;
    private List<Genre> genres;


    @Setter
    @Getter
    @NotNull
    private Rating mpa;

    @Getter
    private List<Long> idUserWhoLikedSet;

    public void addLike(long userId) {
        idUserWhoLikedSet.add(userId);
    }

    public void deleteLike(long userId) throws UserIsNotExistingException {
        if (!idUserWhoLikedSet.contains(userId)) {
            throw new UserIsNotExistingException(String.format("Пользователь с id = %d не ставил лайк", userId));
        }
        idUserWhoLikedSet.remove(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Film film = (Film) o;

        return id == film.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
