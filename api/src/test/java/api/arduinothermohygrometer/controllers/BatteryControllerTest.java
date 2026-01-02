package api.arduinothermohygrometer.controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import api.arduinothermohygrometer.controllers.implementations.BatteryControllerImpl;
import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.exceptions.GlobalExceptionHandler;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.services.BatteryService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for BatteryControllerImpl")
@ExtendWith(MockitoExtension.class)
class BatteryControllerTest {
    @Mock
    private BatteryService batteryService;

    private MockMvcTester mockMvcTester;

    @BeforeEach
    void setup() {
        mockMvcTester = MockMvcTester.of(new BatteryControllerImpl(batteryService), new GlobalExceptionHandler());
    }

    @Test
    @DisplayName("getBatteryById returns 200 OK with valid id.")
    void givenValidId_whenGettingBatteryById_thenReturn200OK() {
        UUID id = UUID.randomUUID();
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        when(batteryService.getBatteryDtoById(id)).thenReturn(batteryDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/batteries/id/{id}", id)
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.registeredAt",
                path -> assertThat(path).asString().isEqualTo(registeredAt.toString()))
            .hasPathSatisfying("$.batteryStatus",
                path -> assertThat(path).asNumber().isEqualTo(batteryStatus));
    }

    @Test
    @DisplayName("getBatteryById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenGettingBatteryById_thenReturn404NotFound() {
        UUID id = new UUID(0, 0);
        when(batteryService.getBatteryDtoById(id))
            .thenThrow(new ResourceNotFoundException("Battery with id=" + id + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/batteries/id/{id}", id)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Battery with id=" + id + " not found.");
    }

    @Test
    @DisplayName("getBatteryByTimestamp returns 200 OK with valid timestamp.")
    void givenValidTimestamp_whenGettingBatteryByTimestamp_thenReturn200OK() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(timestamp)
                                          .batteryStatus(batteryStatus)
                                          .build();
        when(batteryService.getBatteryDtoByTimestamp(timestamp)).thenReturn(batteryDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/batteries/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.registeredAt",
                path -> assertThat(path).asString().isEqualTo(timestamp.toString()))
            .hasPathSatisfying("$.batteryStatus",
                path -> assertThat(path).asNumber().isEqualTo(batteryStatus));
    }

    @Test
    @DisplayName("getBatteryByTimestamp returns 404 NOT FOUND with invalid timestamp.")
    void givenInvalidTimestamp_whenGettingBatteryByTimestamp_thenReturn404NotFound() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        when(batteryService.getBatteryDtoByTimestamp(timestamp))
            .thenThrow(new ResourceNotFoundException("Battery with timestamp=" + timestamp + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/batteries/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Battery with timestamp=" + timestamp + " not found.");
    }

    @Test
    @DisplayName("getBatteryByDate returns 200 OK with valid date.")
    void givenValidDate_whenGettingBatteryByDate_thenReturn200OK() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus = 95;
        int batteryStatus2 = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(timestamp)
                                          .batteryStatus(batteryStatus)
                                          .build();
        BatteryDto batteryDto2 = BatteryDto.builder()
                                           .registeredAt(timestamp.minusHours(1))
                                           .batteryStatus(batteryStatus2)
                                           .build();
        List<BatteryDto> batteries = List.of(batteryDto, batteryDto2);
        when(batteryService.getBatteryDtosByDate(timestamp.toLocalDate())).thenReturn(batteries);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/batteries/date")
                                            .param("date", timestamp.toLocalDate().toString())
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.[0].batteryStatus",
                path -> assertThat(path).asNumber().isEqualTo(batteryStatus))
            .hasPathSatisfying("$.[1].batteryStatus",
                path -> assertThat(path).asNumber().isEqualTo(batteryStatus2));
    }

    @Test
    @DisplayName("getBatteryByDate returns 404 NOT FOUND with invalid date.")
    void givenInvalidDate_whenGettingBatteryByDate_thenReturn404NotFound() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        when(batteryService.getBatteryDtosByDate(timestamp.toLocalDate()))
            .thenThrow(new ResourceNotFoundException("Batteries with date=" + timestamp.toLocalDate() + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/batteries/date")
                                            .param("date", timestamp.toLocalDate().toString())
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Batteries with date=" + timestamp.toLocalDate() + " not found.");
    }

    @Test
    @DisplayName("create returns 201 CREATED for creating valid battery model.")
    void givenValidBatteryDtoModel_whenCreating_thenReturn200OK() {
        UUID id = UUID.randomUUID();
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus = 95;
        String requestBatteryDto = String.format("""
            {
            "registeredAt": "%s",
            "batteryStatus": %d
            }
            """, localDateTime, batteryStatus);
        String responseBatteryDto = String.format("""
            {
            "registeredAt": "%s",
            "batteryStatus": %d
            }
            """, localDateTime, batteryStatus);
        BatteryDto batteryDto = BatteryDto.builder()
                                          .id(id)
                                          .registeredAt(localDateTime)
                                          .batteryStatus(batteryStatus)
                                          .build();
        when(batteryService.createBatteryDto(any())).thenReturn(batteryDto);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/v1/api/batteries/create")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestBatteryDto)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.CREATED)
            .bodyJson()
            .isEqualTo(responseBatteryDto);
    }

    @Test
    @DisplayName("create returns 400 BAD REQUEST for creating invalid batteryDto model.")
    void givenInvalidBatteryDtoModel_whenCreating_thenReturn400BadRequest() {
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus = 105;
        String requestBatteryDto = String.format("""
            {
            "registeredAt": "%s",
            "batteryStatus": %d
            }
            """, localDateTime, batteryStatus);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/v1/api/batteries/create")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestBatteryDto)
                                            .exchange();

        verifyNoInteractions(batteryService);
        assertThat(result)
            .hasStatus(HttpStatus.BAD_REQUEST)
            .bodyJson()
            .hasPathSatisfying("$.detail",
                path -> assertThat(path).asString().isEqualTo("One or more fields are invalid."))
            .hasPathSatisfying("$.title",
                path -> assertThat(path).asString().isEqualTo("Model validation error."))
            .hasPathSatisfying("$.errors.batteryStatus",
                path -> assertThat(path).asString().isNotBlank());
    }

    @Test
    @DisplayName("deleteBatteryById returns 204 NO CONTENT with valid id.")
    void givenValidId_whenDeletingBatteryById_thenReturn204NoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(batteryService).deleteBatteryById(id);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/batteries/delete/id/{id}", id)
                                            .exchange();

        verify(batteryService, times(1)).deleteBatteryById(id);
        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteBatteryById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenDeletingBatteryById_thenReturn404NotFound() {
        UUID id = new UUID(0, 0);
        doThrow(new ResourceNotFoundException("Battery with id=" + id + " not found.")).when(batteryService)
                                                                                       .deleteBatteryById(id);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/batteries/delete/id/{id}", id)
                                            .exchange();

        verify(batteryService, times(1)).deleteBatteryById(id);
        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Battery with id=" + id + " not found.");
    }

    @Test
    @DisplayName("deleteBatteryByTimestamp returns 204 NO CONTENT with valid timestamp.")
    void givenValidTimestamp_whenDeletingBatteryByTimestamp_thenReturn204NoContent() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doNothing().when(batteryService).deleteBatteryByTimestamp(timestamp);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/batteries/delete/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        verify(batteryService, times(1)).deleteBatteryByTimestamp(timestamp);
        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteBatteryByTimestamp returns 404 NOT FOUND with invalid timestamp.")
    void givenInvalidTimestamp_whenDeletingBatteryByTimestamp_thenReturn404NotFound() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doThrow(new ResourceNotFoundException(
            "Battery with timestamp=" + timestamp + " not found.")).when(batteryService)
                                                                   .deleteBatteryByTimestamp(timestamp);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/batteries/delete/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        verify(batteryService, times(1)).deleteBatteryByTimestamp(timestamp);
        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Battery with timestamp=" + timestamp + " not found.");
    }
}
