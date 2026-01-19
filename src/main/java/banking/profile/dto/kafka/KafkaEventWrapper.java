package banking.profile.dto.kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KafkaEventWrapper {
    @JsonProperty("data")
    public UserCreatedEvent data;

    @JsonProperty("eventType")
    public String eventType;
}
