package banking.profile.kafka;

import banking.profile.dto.kafka.KafkaEventWrapper;
import banking.profile.dto.kafka.UserCreatedEvent;
import banking.profile.service.ClientProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedConsumer {
    private final ObjectMapper objectMapper;
    private final ClientProfileService clientProfileService;

    @KafkaListener(
            topics = "${banking.kafka.topics.users}",
            groupId = "${spring.application.name}",
            containerFactory = "kafkaListenerManualCommitContainerFactory"
    )
    public void handleUserCreated(
            @Payload String message,
            ConsumerRecord<String, String> record,
            Acknowledgment acknowledgment
    ) {
        String messageId = String.format("%s-%d-%d", record.topic(), record.partition(), record.offset());

        try {
            log.info("Получено событие Kafka [id={}]: partition={}, offset={}",
                    messageId, record.partition(), record.offset());

            KafkaEventWrapper wrapper = objectMapper.readValue(
                    message,
                    KafkaEventWrapper.class
            );

            if (!"USER_CREATED".equals(wrapper.getEventType())) {
                log.debug("Пропускаем событие с типом: {}", wrapper.getEventType());
                acknowledgment.acknowledge();
                return;
            }

            if (wrapper.getData() == null) {
                log.error("Поле data пустое для USER_CREATED события");
                throw new IllegalArgumentException("Data is null for USER_CREATED event");
            }

            UserCreatedEvent event = wrapper.getData();
            if (event.getUserId() == null || event.getEmail() == null) {
                log.error("Недостаточно данных: userId={}, email={}",
                        event.getUserId(), event.getEmail());
                throw new IllegalArgumentException("Missing required fields: userId or email");
            }

            clientProfileService.createProfile(event.getUserId(), event.getEmail());
            acknowledgment.acknowledge();
            log.info("Обработано событие создания пользователя: userId={}, messageId={}",
                    event.getUserId(), messageId);
        } catch (Exception exception) {
            log.error("Не удалось обработать событие [id={}] по созданию пользователя: {}",
                    messageId, exception.getMessage(), exception);
            throw new RuntimeException(exception);
        }
    }
}
