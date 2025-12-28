package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClientProfileNotFoundException extends RuntimeException {
    public ClientProfileNotFoundException(String email) {super("Профиль не найден с email: " + email);}
}
