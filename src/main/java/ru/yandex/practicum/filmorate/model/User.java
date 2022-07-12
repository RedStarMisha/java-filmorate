package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.filmorate.exceptions.UsersIsNotFriendsException;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
    @Getter
    @Setter
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
    private final Map<Long, Boolean> friendsStatus = new HashMap<>();


    public void addFriend(long id) {
        friendsId.add(id);
    }

    public void deleteFriend(long friendId) {
        if (!friendsId.contains(friendId)) {
            throw new UsersIsNotFriendsException(this.id, friendId);
        }
        friendsId.remove(friendId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id == user.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
