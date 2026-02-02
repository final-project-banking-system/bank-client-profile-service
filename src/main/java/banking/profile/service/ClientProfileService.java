package banking.profile.service;

import banking.profile.dto.request.ClientProfileRequest;
import banking.profile.dto.response.ClientProfileResponse;
import banking.profile.error.exception.ClientProfileNotFoundException;
import banking.profile.mapper.ClientProfileMapper;
import banking.profile.model.ClientProfileEntity;
import banking.profile.repository.ClientProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сервис отвечает за управление клиентскими данными
 */
@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClientProfileService {
    private final ClientProfileRepository clientProfileRepository;
    private final ClientProfileMapper clientProfileMapper;

    /**
     * Получает профиль клиента по его userId.
     *
     * @param userId userId клиента
     * @return DTO c данными по профилю клиента
     */
    @Cacheable(
            value = "profiles",
            key = "#userId.toString()"
    )
    public ClientProfileResponse getProfileByUserId(UUID userId) {
        log.info("Попытка поиска профиля клиента по userId: {}", userId);
        ClientProfileEntity clientProfileEntity = clientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ClientProfileNotFoundException(userId));

        return clientProfileMapper.toResponse(clientProfileEntity);
    }

    /**
     * Обновляет профиль клиента по его userId.
     *
     * @param userId userId пользователя
     * @param request DTO с обновленным данными пользователя
     * @return обновленный DTO пользователя
     */
    @Transactional
    @CacheEvict(
            value = "profiles",
            key = "#userId.toString()"
    )
    public ClientProfileResponse updateProfile(UUID userId, ClientProfileRequest request) {
        log.info("Обновление профиля клиента с userId: {}", userId);

        ClientProfileEntity clientProfile = clientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ClientProfileNotFoundException(userId));

        if (request.getEmail() != null) {
            clientProfile.setEmail(request.getEmail());
        }

        if (request.getPhone() != null) {
            clientProfile.setPhone(request.getPhone());
        }

        if (request.getBirthDate() != null) {
            clientProfile.setBirthDate(request.getBirthDate());
        }

        if (request.getFirstName() != null) {
            clientProfile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            clientProfile.setLastName(request.getLastName());
        }
        if (request.getMiddleName() != null) {
            clientProfile.setMiddleName(request.getMiddleName());
        }

        clientProfile.setUpdatedAt(LocalDateTime.now());

        ClientProfileEntity clientProfileUpdated = clientProfileRepository.save(clientProfile);
        log.info("Профиль клиента обновлен: {}", userId);

        return clientProfileMapper.toResponse(clientProfileUpdated);
    }

    /**
     * Обновляет профиль клиента по его userId.
     *
     * @param userId userId пользователя
     * @param email email пользователя
     */
    @Transactional
    public void createProfile(UUID userId, String email) {
        ClientProfileEntity profile = new ClientProfileEntity();
        profile.setUserId(userId);
        profile.setEmail(email);
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        clientProfileRepository.save(profile);
    }
}
