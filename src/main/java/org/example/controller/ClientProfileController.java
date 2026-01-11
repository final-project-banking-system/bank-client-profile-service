package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.ClientProfileRequest;
import org.example.dto.response.ClientProfileResponse;
import org.example.service.ClientProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
     * @param userId userId клиента
     * @return DTO с информацией о клиенте
     */
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ClientProfileResponse getClientProfile(@PathVariable UUID userId) {
        return clientProfileService.getProfileByUserId(userId);
    }

    /**
     * Обновляет информацию о клиенте
     * @param userId userId клиента
     * @return ResponseEntity с обновленным DTO клиента
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ClientProfileResponse> updateClientProfile(
            @PathVariable UUID userId, @RequestBody ClientProfileRequest request
    ) {
        return ResponseEntity.ok(clientProfileService.updateProfile(userId, request));
    }
}
