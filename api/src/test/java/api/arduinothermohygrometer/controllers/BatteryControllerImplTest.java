package api.arduinothermohygrometer.controllers;

import java.time.LocalDate;
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
import tools.jackson.databind.ObjectMapper;

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
class BatteryControllerImplTest {
    @Mock
    private BatteryService batteryService;

    private ObjectMapper objectMapper;

    private MockMvcTester mockMvcTester;

    @BeforeEach
    void setup() {
        mockMvcTester = MockMvcTester.of(new BatteryControllerImpl(batteryService), new GlobalExceptionHandler());
        objectMapper = new ObjectMapper();
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
        when(batteryService.getBatteryById(id)).thenReturn(batteryDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/batteries/{id}", id)
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPath("$.registeredAt")
            .hasPathSatisfying("$.batteryStatus",
                path -> assertThat(path).asNumber().isEqualTo(batteryStatus));
    }

    @Test
    @DisplayName("getBatteryById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenGettingBatteryById_thenReturn404NotFound() {
        UUID id = new UUID(0, 0);
        when(batteryService.getBatteryById(id))
            .thenThrow(new ResourceNotFoundException("Battery with id=" + id + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/batteries/{id}", id)
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
        when(batteryService.getBatteryByTimestamp(timestamp)).thenReturn(batteryDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/batteries/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPath("$.registeredAt")
            .hasPathSatisfying("$.batteryStatus",
                path -> assertThat(path).asNumber().isEqualTo(batteryStatus));
    }

    @Test
    @DisplayName("getBatteryByTimestamp returns 404 NOT FOUND with invalid timestamp.")
    void givenInvalidTimestamp_whenGettingBatteryByTimestamp_thenReturn404NotFound() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        when(batteryService.getBatteryByTimestamp(timestamp))
            .thenThrow(new ResourceNotFoundException("Battery with timestamp=" + timestamp + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/batteries/timestamp")
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
        LocalDateTime timestamp2 = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus2 = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(timestamp)
                                          .batteryStatus(batteryStatus)
                                          .build();
        BatteryDto batteryDto2 = BatteryDto.builder()
                                           .registeredAt(timestamp2)
                                           .batteryStatus(batteryStatus2)
                                           .build();
        List<BatteryDto> batteries = List.of(batteryDto, batteryDto2);
        when(batteryService.getBatteriesByDate(timestamp.toLocalDate())).thenReturn(batteries);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/batteries/date")
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
        LocalDate date = LocalDate.now();
        when(batteryService.getBatteriesByDate(date))
            .thenThrow(new ResourceNotFoundException("Batteries with date=" + date + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/batteries/date")
                                            .param("date", date.toString())
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Batteries with date=" + date + " not found.");
    }

    @Test
    @DisplayName("create returns 201 CREATED for creating valid battery model.")
    void givenValidBatteryDtoModel_whenCreating_thenReturn201CREATED() {
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus = 95;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        when(batteryService.createBattery(any())).thenReturn(batteryDto);
        String requestJson = objectMapper.writeValueAsString(batteryDto);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/api/batteries")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestJson)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.CREATED)
            .bodyJson()
            .hasPath("$.registeredAt")
            .hasPathSatisfying("$.batteryStatus",
                path -> assertThat(path).asNumber().isEqualTo(batteryStatus));
    }

    @Test
    @DisplayName("create returns 400 BAD REQUEST for creating invalid batteryDto model.")
    void givenInvalidBatteryDtoModel_whenCreating_thenReturn400BadRequest() {
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus = 105;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        String requestJson = objectMapper.writeValueAsString(batteryDto);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/api/batteries")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestJson)
                                            .exchange();

        verifyNoInteractions(batteryService);
        assertThat(result)
            .hasStatus(HttpStatus.BAD_REQUEST)
            .bodyJson()
            .hasPathSatisfying("$.detail",
                path -> assertThat(path).asString().isEqualTo("One or more fields are invalid."))
            .hasPathSatisfying("$.title",
                path -> assertThat(path).asString().isEqualTo("Entity validation error."))
            .hasPathSatisfying("$.errors.batteryStatus",
                path -> assertThat(path).asString().isNotBlank());
    }

    @Test
    @DisplayName("deleteBatteryById returns 204 NO CONTENT with valid id.")
    void givenValidId_whenDeletingBatteryById_thenReturn204NoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(batteryService).deleteBatteryById(id);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/batteries/{id}", id)
                                            .exchange();

        verify(batteryService, times(1)).deleteBatteryById(id);
        assertThat(result)
            .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteBatteryById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenDeletingBatteryById_thenReturn404NotFound() {
        UUID id = new UUID(0, 0);
        doThrow(new ResourceNotFoundException("Battery with id=" + id + " not found.")).when(batteryService)
                                                                                       .deleteBatteryById(id);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/batteries/{id}", id)
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
                                            .uri("/api/batteries/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        verify(batteryService, times(1)).deleteBatteryByTimestamp(timestamp);
        assertThat(result)
            .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteBatteryByTimestamp returns 404 NOT FOUND with invalid timestamp.")
    void givenInvalidTimestamp_whenDeletingBatteryByTimestamp_thenReturn404NotFound() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doThrow(new ResourceNotFoundException(
            "Battery with timestamp=" + timestamp + " not found.")).when(batteryService)
                                                                   .deleteBatteryByTimestamp(timestamp);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/batteries/timestamp")
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
