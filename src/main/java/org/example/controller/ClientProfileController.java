package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.ClientProfileRequest;
import org.example.dto.response.ClientProfileResponse;
import org.example.service.ClientProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления профилями клиентов
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class ClientProfileController {
    private final ClientProfileService clientProfileService;

    /**
     * Получает информацию о клиенте
     * @param email email клиента
     * @return DTO с информацией о клиенте
     */
    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ClientProfileResponse getClientProfile(@PathVariable String email) {
        return clientProfileService.getProfileByEmail(email);
    }

    /**
     * Обновляет информацию о клиенте
     * @param email email клиента
     * @return ResponseEntity с обновленным DTO клиента
     */
    @PutMapping("/{email}")
    public ResponseEntity<ClientProfileResponse> updateClientProfile(
            @PathVariable String email, @RequestBody ClientProfileRequest request
    ) {
        return ResponseEntity.ok(clientProfileService.updateProfile(email, request));
    }
}
