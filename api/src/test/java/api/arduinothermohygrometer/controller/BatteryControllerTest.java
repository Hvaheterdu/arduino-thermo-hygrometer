package api.arduinothermohygrometer.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import api.arduinothermohygrometer.base.WebMvcTestBase;
import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.service.BatteryService;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@DisplayName("BatteryController MVC slice unit tests.")
@WebMvcTest(BatteryController.class)
class BatteryControllerTest extends WebMvcTestBase {
    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BatteryService batteryService;

    @Test
    @DisplayName("getBatteryById returns 200 OK with valid id.")
    void givenValidId_whenGetBatteryById_thenReturn200OK() {
        UUID id = UUID.randomUUID();
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        when(batteryService.getBatteryById(id)).thenReturn(batteryDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/v1/batteries/{id}", id)
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
    void givenInvalidId_whenGetBatteryById_thenReturn404NotFound() {
        UUID invalidId = new UUID(0, 0);
        when(batteryService.getBatteryById(invalidId))
            .thenThrow(new ResourceNotFoundException("Battery with id=" + invalidId + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/v1/batteries/{id}", invalidId)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Battery with id=" + invalidId + " not found.");
    }

    @Test
    @DisplayName("getBatteriesByDateOrTimestamp returns 200 OK with valid registeredAt.")
    void givenValidRegisteredAt_whenGetBatteriesByDateOrTimestamp_thenReturn200OK() {
        boolean dateOnly = true;
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus = 95;
        int batteryStatus2 = 90;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        BatteryDto batteryDto2 = BatteryDto.builder()
                                           .registeredAt(registeredAt.minusHours(1))
                                           .batteryStatus(batteryStatus2)
                                           .build();
        List<BatteryDto> batteryDtos = List.of(batteryDto, batteryDto2);
        when(batteryService.getBatteriesByDateOrTimestamp(registeredAt, dateOnly)).thenReturn(batteryDtos);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/v1/batteries")
                                            .param("registeredAt", registeredAt.toString())
                                            .param("dateOnly", String.valueOf(dateOnly))
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
    @DisplayName("getBatteriesByDateOrTimestamp returns 404 NOT FOUND with invalid registeredAt.")
    void givenInvalidRegisteredAt_whenGetBatteriesByDateOrTimestamp_thenReturn404NotFound() {
        boolean dateOnly = true;
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        when(batteryService.getBatteriesByDateOrTimestamp(registeredAt, dateOnly))
            .thenThrow(new ResourceNotFoundException("Batteries registeredAt=" + registeredAt + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/v1/batteries")
                                            .param("registeredAt", registeredAt.toString())
                                            .param("dateOnly", String.valueOf(dateOnly))
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Batteries registeredAt=" + registeredAt + " not found.");
    }

    @Test
    @DisplayName("createBattery returns 201 CREATED for creating valid battery model.")
    void givenValidBatteryDtoModel_whenCreateBattery_thenReturn201CREATED() {
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus = 95;
        BatteryDto batteryDto = BatteryDto.builder()
                                          .registeredAt(registeredAt)
                                          .batteryStatus(batteryStatus)
                                          .build();
        when(batteryService.createBattery(any())).thenReturn(batteryDto);
        String requestJson = objectMapper.writeValueAsString(batteryDto);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/api/v1/batteries")
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
    @DisplayName("createBattery returns 400 BAD REQUEST for attempting to create invalid battery dto.")
    void givenInvalidBatteryDto_whenCreateBattery_thenReturn400BadRequest() {
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        int batteryStatus = 105;
        BatteryDto invalidBatteryDto = BatteryDto.builder()
                                                 .registeredAt(registeredAt)
                                                 .batteryStatus(batteryStatus)
                                                 .build();
        String requestJson = objectMapper.writeValueAsString(invalidBatteryDto);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/api/v1/batteries")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestJson)
                                            .exchange();

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
    void givenValidId_whenDeleteBatteryById_thenReturn204NoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(batteryService).deleteBatteryById(id);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/v1/batteries/{id}", id)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteBatteryById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenDeleteBatteryById_thenReturn404NotFound() {
        UUID invalidId = new UUID(0, 0);
        doThrow(new ResourceNotFoundException("Battery with id=" + invalidId + " not found.")).when(batteryService).deleteBatteryById(invalidId);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/v1/batteries/{id}", invalidId)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Battery with id=" + invalidId + " not found.");
    }

    @Test
    @DisplayName("deleteBatteriesByDateOrTimestamp returns 204 NO CONTENT with valid registeredAt.")
    void givenValidRegisteredAt_whenDeleteBatteriesByDateOrTimestamp_thenReturn204NoContent() {
        boolean dateOnly = false;
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doNothing().when(batteryService).deleteBatteriesByDateOrTimestamp(registeredAt, dateOnly);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/v1/batteries")
                                            .param("registeredAt", registeredAt.toString())
                                            .param("dateOnly", String.valueOf(dateOnly))
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteBatteriesByDateOrTimestamp returns 404 NOT FOUND with invalid registeredAt.")
    void givenInvalidRegisteredAt_whenDeleteBatteriesByDateOrTimestamp_thenReturn404NotFound() {
        boolean dateOnly = false;
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doThrow(new ResourceNotFoundException("Batteries registeredAt=" + registeredAt + " not found."))
            .when(batteryService).deleteBatteriesByDateOrTimestamp(registeredAt, dateOnly);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/v1/batteries")
                                            .param("registeredAt", registeredAt.toString())
                                            .param("dateOnly", String.valueOf(dateOnly))
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Batteries registeredAt=" + registeredAt + " not found.");
    }
}
