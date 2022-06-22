package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.exceptions.UserIsNotExistingException;
import ru.yandex.practicum.filmorate.myvalidator.Date;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class Film {
    @Getter
    @Setter
    @PositiveOrZero
    @Max(Integer.MAX_VALUE)
    private long id;

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

    @Getter
    private final Set<Long> idUserWhoLikedSet = new HashSet<>();

    public void addLike(long userId) {
        idUserWhoLikedSet.add(userId);
    }

    public void deleteLike(long userId) {
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
