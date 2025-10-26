package ru.yandex.practicum.catsgram.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final Map<Long, User> users = new HashMap<>();

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользовватель с ID" + id + " не найден.");
        }
        return user;
    }

    public User createUser(User user) {

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }

        if (isEmailExists(user.getEmail())) {
            throw new DuplicatedDataException("Этот имейл уже используется");
        }

        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());

        users.put(user.getId(), user);
        return user;
    }

    public User updateUser(User user) {
        if (user.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }

        User existingUser = users.get(user.getId());
        if (existingUser == null) {
            throw new NotFoundException("Пользователь с ID " + user.getId() + " не найден");
        }

        if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
            if (isEmailExists(user.getEmail())) {
                throw new DuplicatedDataException("Этот имейл уже используется");
            }
        }

        updateUserFields(existingUser, user);
        return existingUser;
    }

    private boolean isEmailExists(String email) {
        return users.values().stream().anyMatch(user -> email.equals(user.getEmail()));
    }

    private void updateUserFields(User existingUser, User newUserData) {
        if (newUserData.getUsername() != null) {
            existingUser.setUsername(newUserData.getUsername());
        }

        if (newUserData.getEmail() != null) {
            existingUser.setEmail(newUserData.getEmail());
        }

        if (newUserData.getPassword() != null) {
            existingUser.setPassword(newUserData.getPassword());
        }
    }
}
