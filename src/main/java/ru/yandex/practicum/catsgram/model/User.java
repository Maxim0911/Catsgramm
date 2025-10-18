package ru.yandex.practicum.catsgram.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(of = { "email" })
public class User {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Instant registrationDate;
}
