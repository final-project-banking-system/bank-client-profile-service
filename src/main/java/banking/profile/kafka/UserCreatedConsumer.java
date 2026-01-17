package banking.profile.kafka;

import banking.profile.dto.kafka.KafkaEventWrapper;
import banking.profile.dto.kafka.UserCreatedEvent;
import banking.profile.service.ClientProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedConsumer {
    private final ObjectMapper objectMapper;
    private final ClientProfileService clientProfileService;

    @KafkaListener(
            topics = "${banking.kafka.topics.users}",
            groupId = "${spring.application.name}"
    )
    public void handleUserCreated(String message) {
        try {
            log.info("Получено событие Kafka: {}", message);

            KafkaEventWrapper wrapper = objectMapper.readValue(
                    message,
                    KafkaEventWrapper.class
            );

            if (!"USER_CREATED".equals(wrapper.getEventType())) {
                log.debug("Пропускаем событие с типом: {}", wrapper.getEventType());
                return;
            }

            if (wrapper.getData() == null) {
                log.error("Поле data пустое для такого типа события: {}", wrapper.getEventType());
                return;
            }

            UserCreatedEvent event = wrapper.getData();

            if (event.getUserId() != null && event.getEmail() != null) {
                clientProfileService.createProfile(event.getUserId(), event.getEmail());
            }
        } catch (Exception exception) {
            log.error("Не удалось обработать событие по созданию пользователя: {}",
                    exception.getMessage(), exception);
        }
    }
}
