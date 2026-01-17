package banking.profile.dto.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent {
    private UUID userId;
    private String login;
    private String email;
    private String role;
}
