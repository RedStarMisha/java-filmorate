package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.exceptions.UsersIsNotFriendsException;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
    @Getter
    @Setter
    @PositiveOrZero
    @Max(Long.MAX_VALUE)
    private long id;

    @Email
    private final String email;

    @NotEmpty
    @Pattern(message = "Не может содержать пробел",regexp = "\\w+")
    private final String login;

    private final String name;

    @NotNull
    @Past
    private final LocalDate birthday;

    private final Set<Long> friendsId = new HashSet<>();

    public void addFriend(long id) {
        friendsId.add(id);
    }

    public void deleteFriend(long friendId) {
        if (!friendsId.contains(friendId)) {
            throw new UsersIsNotFriendsException(this.id, friendId);
        }
        friendsId.remove(friendId);
    }
}
