package banking.profile.kafka;

import banking.profile.service.ClientProfileService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedConsumer {
    private final ObjectMapper objectMapper;
    private final ClientProfileService clientProfileService;

    @KafkaListener(topics = "auth.users", groupId = "client-profile-service")
    public void handleUserCreated(String message) {
        try {
            log.info("Получено событие Kafka: {}", message);
            JsonNode root = objectMapper.readTree(message);
            JsonNode data = root.get("data");

            if (data != null) {
                String userId = data.get("userId").asText();
                String email = data.get("email").asText();

                clientProfileService.createProfile(UUID.fromString(userId), email);
            }
        } catch (Exception exception) {
            log.info("Не удалось обработать событие по создание пользователя.");
            throw new RuntimeException(exception);
        }
    }
}
