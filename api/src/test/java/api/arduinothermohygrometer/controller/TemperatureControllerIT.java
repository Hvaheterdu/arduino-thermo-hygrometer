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
import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.service.TemperatureService;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TemperatureControllerImpl MVC slice integration tests.")
@WebMvcTest(TemperatureController.class)
class TemperatureControllerIT extends WebMvcTestBase {
    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TemperatureService temperatureService;

    @Test
    @DisplayName("getTemperatureById returns 200 OK with valid id.")
    void givenValidId_whenGettingTemperatureById_thenReturn200OK() {
        UUID id = UUID.randomUUID();
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double temp = 20.01;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(registeredAt)
                                                      .temp(temp)
                                                      .build();
        when(temperatureService.getTemperatureById(id)).thenReturn(temperatureDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/temperatures/{id}", id)
                                            .header("X-API-KEY", "api-key-secret")
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPath("$.registeredAt")
            .hasPathSatisfying("$.temp",
                path -> assertThat(path).asNumber().isEqualTo(temp));
    }

    @Test
    @DisplayName("getTemperatureById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenGettingTemperatureById_thenReturn404NotFound() {
        UUID invalidId = new UUID(0, 0);
        when(temperatureService.getTemperatureById(invalidId))
            .thenThrow(new ResourceNotFoundException("Temperature with id=" + invalidId + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/temperatures/{id}", invalidId)
                                            .header("X-API-KEY", "api-key-secret")
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Temperature with id=" + invalidId + " not found.");
    }

    @Test
    @DisplayName("getTemperaturesByDateOrTimestamp returns 200 OK with valid dateTime.")
    void givenValidDateTime_whenGettingTemperaturesByDateOrTimestamp_thenReturn200OK() {
        boolean checkOnlyDate = true;
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double temp = 20.01;
        Double temp2 = 90.01;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(dateTime)
                                                      .temp(temp)
                                                      .build();
        TemperatureDto temperatureDto2 = TemperatureDto.builder()
                                                       .registeredAt(dateTime.minusHours(1))
                                                       .temp(temp2)
                                                       .build();
        List<TemperatureDto> temperatureDtos = List.of(temperatureDto, temperatureDto2);
        when(temperatureService.getTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate)).thenReturn(temperatureDtos);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/temperatures")
                                            .header("X-API-KEY", "api-key-secret")
                                            .param("dateTime", dateTime.toString())
                                            .param("checkOnlyDate", String.valueOf(checkOnlyDate))
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.[0].temp",
                path -> assertThat(path).asNumber().isEqualTo(temp))
            .hasPathSatisfying("$.[1].temp",
                path -> assertThat(path).asNumber().isEqualTo(temp2));
    }

    @Test
    @DisplayName("getTemperaturesByDateOrTimestamp returns 404 NOT FOUND with invalid dateTime.")
    void givenInvalidDateTime_whenGettingTemperaturesByDateOrTimestamp_thenReturn404NotFound() {
        boolean checkOnlyDate = true;
        LocalDateTime invalidDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        when(temperatureService.getTemperaturesByDateOrTimestamp(invalidDateTime, checkOnlyDate))
            .thenThrow(new ResourceNotFoundException("Temperatures with dateTime=" + invalidDateTime + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/temperatures")
                                            .header("X-API-KEY", "api-key-secret")
                                            .param("dateTime", invalidDateTime.toString())
                                            .param("checkOnlyDate", String.valueOf(checkOnlyDate))
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Temperatures with dateTime=" + invalidDateTime + " not found.");
    }

    @Test
    @DisplayName("create returns 201 CREATED for creating valid temperature model.")
    void givenValidTemperatureDtoModel_whenCreating_thenReturn201CREATED() {
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double temp = 21.02;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(registeredAt)
                                                      .temp(temp)
                                                      .build();
        when(temperatureService.createTemperature(any())).thenReturn(temperatureDto);
        String requestJson = objectMapper.writeValueAsString(temperatureDto);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/api/temperatures")
                                            .header("X-API-KEY", "api-key-secret")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestJson)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.CREATED)
            .bodyJson()
            .hasPath("$.registeredAt")
            .hasPathSatisfying("$.temp",
                path -> assertThat(path).asNumber().isEqualTo(temp));
    }

    @Test
    @DisplayName("create returns 400 BAD REQUEST for creating invalid temperature model.")
    void givenInvalidTemperatureDtoModel_whenCreating_thenReturn400BadRequest() {
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double temp = 150.03;
        TemperatureDto invalidTemperatureDto = TemperatureDto.builder()
                                                             .registeredAt(registeredAt)
                                                             .temp(temp)
                                                             .build();
        String requestJson = objectMapper.writeValueAsString(invalidTemperatureDto);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/api/temperatures")
                                            .header("X-API-KEY", "api-key-secret")
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
            .hasPathSatisfying("$.errors.temp",
                path -> assertThat(path).asString().isNotBlank());
    }

    @Test
    @DisplayName("deleteTemperatureById returns 204 NO CONTENT with valid id.")
    void givenValidId_whenDeletingTemperatureById_thenReturn204NoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(temperatureService).deleteTemperatureById(id);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/temperatures/{id}", id)
                                            .header("X-API-KEY", "api-key-secret")
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteTemperatureById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenDeletingTemperatureById_thenReturn404NotFound() {
        UUID invalidId = new UUID(0, 0);
        doThrow(new ResourceNotFoundException("Temperature with id=" + invalidId + " not found.")).when(temperatureService)
                                                                                                  .deleteTemperatureById(invalidId);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/temperatures/{id}", invalidId)
                                            .header("X-API-KEY", "api-key-secret")
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Temperature with id=" + invalidId + " not found.");
    }

    @Test
    @DisplayName("deleteTemperaturesByDateOrTimestamp returns 204 NO CONTENT with valid dateTime.")
    void givenValidDateTime_whenDeletingTemperaturesByDateOrTimestamp_thenReturn204NoContent() {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doNothing().when(temperatureService).deleteTemperaturesByDateOrTimestamp(dateTime, checkOnlyDate);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/temperatures")
                                            .header("X-API-KEY", "api-key-secret")
                                            .param("dateTime", dateTime.toString())
                                            .param("checkOnlyDate", String.valueOf(checkOnlyDate))
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteTemperaturesByDateOrTimestamp returns 404 NOT FOUND with invalid dateTime.")
    void givenInvalidDateTime_whenDeletingTemperaturesByDateOrTimestamp_thenReturn404NotFound() {
        boolean checkOnlyDate = false;
        LocalDateTime invalidDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doThrow(new ResourceNotFoundException("Temperatures with dateTime=" + invalidDateTime + " not found."))
            .when(temperatureService).deleteTemperaturesByDateOrTimestamp(invalidDateTime, checkOnlyDate);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/temperatures")
                                            .header("X-API-KEY", "api-key-secret")
                                            .param("dateTime", invalidDateTime.toString())
                                            .param("checkOnlyDate", String.valueOf(checkOnlyDate))
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Temperatures with dateTime=" + invalidDateTime + " not found.");
    }
}
