package ru.yandex.practicum.catsgram.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.service.UserService;
import java.util.List;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public  User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
    @ExceptionHandler(ConditionsNotMetException.class)
    public String handleConditionsNotMet(ConditionsNotMetException e) {
        return e.getMessage();
    }

    @ExceptionHandler(DuplicatedDataException.class)
    public String handleDuplicatedData(DuplicatedDataException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException e) {
        return e.getMessage();
    }
}