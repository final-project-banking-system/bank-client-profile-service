package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientProfileNotFoundException extends RuntimeException {
    public ClientProfileNotFoundException(UUID userId) {super("Профиль не найден с userId: " + userId);}
}
