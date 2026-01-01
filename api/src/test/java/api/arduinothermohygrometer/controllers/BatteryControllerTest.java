package api.arduinothermohygrometer.controllers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import api.arduinothermohygrometer.controllers.implementations.BatteryControllerImpl;
import api.arduinothermohygrometer.dtos.BatteryDto;
import api.arduinothermohygrometer.exceptions.GlobalExceptionHandler;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.services.BatteryService;

import static org.assertj.core.api.Assertions.assertThat;
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
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(localDateTime)
                                          .batteryStatus(batteryStatus)
                                          .build();
        when(batteryService.getBatteryDtoById(id)).thenReturn(batteryDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/batteries/id/{id}", id)
                                            .exchange();

        assertThat(result).hasStatusOk()
                          .bodyJson()
                          .hasPathSatisfying("$.registeredAt",
                              path -> assertThat(path).asString().isEqualTo(localDateTime.toString()))
                          .hasPathSatisfying("$.batteryStatus",
                              path -> assertThat(path).asNumber().isEqualTo(batteryStatus));
    }

    @Test
    @DisplayName("getBatteryById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenGettingBatteryById_thenReturn404NotFound() {
        UUID id = new UUID(0, 0);
        when(batteryService.getBatteryDtoById(id)).thenThrow(new ResourceNotFoundException(
            "Battery with id=" + id + " not found"));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/batteries/id/{id}", id)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Battery with id=" + id + " not found");
    }
}
