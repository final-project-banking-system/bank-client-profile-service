package banking.profile.controller;

import banking.profile.dto.request.ClientProfileRequest;
import banking.profile.dto.response.ClientProfileResponse;
import banking.profile.service.ClientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
     * @param jwt jwt токен
     * @return DTO с информацией о клиенте
     */
    @GetMapping
    public ClientProfileResponse getClientProfile(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return clientProfileService.getProfileByUserId(userId);
    }

    /**
     * Обновляет информацию о клиенте
     * @param jwt jwt токен
     * @return ResponseEntity с обновленным DTO клиента
     */
    @PutMapping
    public ResponseEntity<ClientProfileResponse> updateClientProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid ClientProfileRequest request
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(clientProfileService.updateProfile(userId, request));
    }
}
