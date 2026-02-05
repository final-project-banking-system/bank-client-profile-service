package unit;

import banking.profile.dto.request.ClientProfileRequest;
import banking.profile.dto.response.ClientProfileResponse;
import banking.profile.error.exception.ClientProfileNotFoundException;
import banking.profile.mapper.ClientProfileMapper;
import banking.profile.model.ClientProfileEntity;
import banking.profile.repository.ClientProfileRepository;
import banking.profile.service.ClientProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientProfileServiceUnitTest {
    @Mock
    private ClientProfileRepository clientProfileRepository;

    @Mock
    private ClientProfileMapper clientProfileMapper;

    @InjectMocks
    private ClientProfileService clientProfileService;

    private ClientProfileEntity testClientProfileEntity;
    private ClientProfileResponse testClientProfileResponse;
    private final String EMAIL = "test@example.com";
    private final UUID PROFILE_ID = UUID.randomUUID();
    private final UUID USER_ID = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        testClientProfileEntity = new ClientProfileEntity();
        testClientProfileEntity.setId(PROFILE_ID);
        testClientProfileEntity.setUserId(USER_ID);
        testClientProfileEntity.setEmail(EMAIL);
        testClientProfileEntity.setPhone("123456789");
        testClientProfileEntity.setFirstName("FirstName");
        testClientProfileEntity.setLastName("LastName");
        testClientProfileEntity.setBirthDate(LocalDate.of(2000, 1, 1));

        testClientProfileEntity.setCreatedAt(LocalDateTime.now().minusDays(1));
        testClientProfileEntity.setUpdatedAt(LocalDateTime.now().minusDays(1));

        testClientProfileResponse = new ClientProfileResponse();
        testClientProfileResponse.setId(PROFILE_ID);
        testClientProfileResponse.setEmail(EMAIL);
        testClientProfileResponse.setPhone("123456789");
        testClientProfileResponse.setFirstName("FirstName");
        testClientProfileResponse.setLastName("LastName");
        testClientProfileResponse.setBirthDate(LocalDate.of(2000, 1, 1));

        testClientProfileResponse.setCreatedAt(LocalDateTime.now().minusDays(1));
        testClientProfileResponse.setUpdatedAt(LocalDateTime.now().minusDays(1));
    }

    @Test
    public void testGetProfileByEmail_WhenProfileExists_ShouldReturnProfile() {
        when(clientProfileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(testClientProfileEntity));
        when(clientProfileMapper.toResponse(testClientProfileEntity))
                .thenReturn(testClientProfileResponse);

        ClientProfileResponse response = clientProfileService.getProfileByUserId(USER_ID);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(PROFILE_ID);
        assertThat(response.getEmail()).isEqualTo(EMAIL);
        verify(clientProfileRepository, times(1)).findByUserId(USER_ID);
        verify(clientProfileMapper, times(1)).toResponse(testClientProfileEntity);
    }

    @Test
    public void testGetProfileByEmail_WhenProfileDoesNotExist_ShouldThrowException() {
        when(clientProfileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientProfileService.getProfileByUserId(USER_ID))
                .isInstanceOf(ClientProfileNotFoundException.class)
                .hasMessageContaining(USER_ID.toString());

        verify(clientProfileRepository, times(1)).findByUserId(USER_ID);
        verify(clientProfileMapper, never()).toResponse(any());
    }

    @Test
    public void testUpdateProfileByEmail_WhenProfileExists_ShouldUpdateProfile() {
        ClientProfileRequest request = new ClientProfileRequest();
        request.setFirstName("FirstName_Updated");
        request.setLastName("LastName_Updated");
        request.setPhone("123456789_Updated");

        when(clientProfileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(testClientProfileEntity));

        ClientProfileEntity savedEntity = new ClientProfileEntity();
        when(clientProfileRepository.save(any(ClientProfileEntity.class)))
                .thenAnswer(invocation -> {
                    ClientProfileEntity entity = invocation.getArgument(0);
                    savedEntity.setId(entity.getId());
                    savedEntity.setUserId(entity.getUserId());
                    savedEntity.setFirstName(entity.getFirstName());
                    savedEntity.setLastName(entity.getLastName());
                    savedEntity.setPhone(entity.getPhone());
                    savedEntity.setBirthDate(entity.getBirthDate());
                    savedEntity.setEmail(entity.getEmail());
                    savedEntity.setCreatedAt(entity.getCreatedAt());
                    savedEntity.setUpdatedAt(entity.getUpdatedAt());
                    return savedEntity;
                });

        ClientProfileResponse updatedClientProfileResponse = new ClientProfileResponse();
        updatedClientProfileResponse.setId(PROFILE_ID);
        updatedClientProfileResponse.setUserId(USER_ID);
        updatedClientProfileResponse.setEmail(EMAIL);
        updatedClientProfileResponse.setPhone("123456789_Updated");
        updatedClientProfileResponse.setFirstName("FirstName_Updated");
        updatedClientProfileResponse.setLastName("LastName_Updated");
        updatedClientProfileResponse.setBirthDate(LocalDate.of(2000, 1, 1));
        updatedClientProfileResponse.setCreatedAt(LocalDateTime.now().minusDays(1));
        updatedClientProfileResponse.setUpdatedAt(LocalDateTime.now());

        when(clientProfileMapper.toResponse(any(ClientProfileEntity.class)))
                .thenReturn(updatedClientProfileResponse);

        ClientProfileResponse response = clientProfileService.updateProfile(USER_ID, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(PROFILE_ID);
        assertThat(response.getEmail()).isEqualTo(EMAIL);
        assertThat(response.getPhone()).isEqualTo("123456789_Updated");
        assertThat(response.getFirstName()).isEqualTo("FirstName_Updated");
        assertThat(response.getLastName()).isEqualTo("LastName_Updated");
        assertThat(response.getBirthDate()).isEqualTo(LocalDate.of(2000, 1, 1));

        verify(clientProfileRepository).save(any(ClientProfileEntity.class));
    }

    @Test
    public void testUpdateProfileByEmail_WhenProfileDoesNotExist_ShouldThrowException() {
        when(clientProfileRepository.findByUserId(USER_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientProfileService.updateProfile(USER_ID, new ClientProfileRequest()))
                .isInstanceOf(ClientProfileNotFoundException.class)
                .hasMessageContaining(USER_ID.toString());

        verify(clientProfileRepository, times(1)).findByUserId(USER_ID);
        verify(clientProfileMapper, never()).toResponse(any());
    }
}
