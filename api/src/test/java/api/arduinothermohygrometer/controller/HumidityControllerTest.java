package api.arduinothermohygrometer.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.service.HumidityService;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for HumidityControllerImpl.")
@WebMvcTest(HumidityController.class)
class HumidityControllerTest {
    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HumidityService humidityService;

    @Test
    @DisplayName("getHumidityById returns 200 OK with valid id.")
    void givenValidId_whenGettingHumidityById_thenReturn200OK() {
        UUID id = UUID.randomUUID();
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double airHumidity = 20.01;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(registeredAt)
                                             .airHumidity(airHumidity)
                                             .build();
        when(humidityService.getHumidityById(id)).thenReturn(humidityDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/humidities/{id}", id)
                                            .header("X-API-KEY", "api-key-secret")
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPath("$.registeredAt")
            .hasPathSatisfying("$.airHumidity",
                path -> assertThat(path).asNumber().isEqualTo(airHumidity));
    }

    @Test
    @DisplayName("getHumidityById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenGettingHumidityById_thenReturn404NotFound() {
        UUID invalidId = new UUID(0, 0);
        when(humidityService.getHumidityById(invalidId))
            .thenThrow(new ResourceNotFoundException("Humidity with id=" + invalidId + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/humidities/{id}", invalidId)
                                            .header("X-API-KEY", "api-key-secret")
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Humidity with id=" + invalidId + " not found.");
    }

    @Test
    @DisplayName("getHumiditiesByDateOrTimestamp returns 200 OK with valid dateTime.")
    void givenValidDateTime_whenGettingHumiditiesByDateOrTimestamp_thenReturn200OK() {
        boolean checkOnlyDate = true;
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double airHumidity = 20.01;
        Double airHumidity2 = 90.01;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(dateTime)
                                             .airHumidity(airHumidity)
                                             .build();
        HumidityDto humidityDto2 = HumidityDto.builder()
                                              .registeredAt(dateTime.minusHours(1))
                                              .airHumidity(airHumidity2)
                                              .build();
        List<HumidityDto> humidities = List.of(humidityDto, humidityDto2);
        when(humidityService.getHumiditiesByDateOrTimestamp(dateTime, checkOnlyDate)).thenReturn(humidities);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/humidities")
                                            .header("X-API-KEY", "api-key-secret")
                                            .param("dateTime", dateTime.toString())
                                            .param("checkOnlyDate", String.valueOf(checkOnlyDate))
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.[0].airHumidity",
                path -> assertThat(path).asNumber().isEqualTo(airHumidity))
            .hasPathSatisfying("$.[1].airHumidity",
                path -> assertThat(path).asNumber().isEqualTo(airHumidity2));
    }

    @Test
    @DisplayName("getHumiditiesByDateOrTimestamp returns 404 NOT FOUND with invalid dateTime.")
    void givenInvalidDateTime_whenGettingHumiditiesByDateOrTimestamp_thenReturn404NotFound() {
        boolean checkOnlyDate = true;
        LocalDateTime invalidDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        when(humidityService.getHumiditiesByDateOrTimestamp(invalidDateTime, checkOnlyDate))
            .thenThrow(new ResourceNotFoundException("Humidities with dateTime=" + invalidDateTime + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/api/humidities")
                                            .header("X-API-KEY", "api-key-secret")
                                            .param("dateTime", invalidDateTime.toString())
                                            .param("checkOnlyDate", String.valueOf(checkOnlyDate))
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Humidities with dateTime=" + invalidDateTime + " not found.");
    }

    @Test
    @DisplayName("create returns 201 CREATED for creating valid humidity model.")
    void givenValidHumidityDtoModel_whenCreating_thenReturn201CREATED() {
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double airHumidity = 21.02;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(registeredAt)
                                             .airHumidity(airHumidity)
                                             .build();
        when(humidityService.createHumidity(any())).thenReturn(humidityDto);
        String requestJson = objectMapper.writeValueAsString(humidityDto);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/api/humidities")
                                            .header("X-API-KEY", "api-key-secret")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestJson)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.CREATED)
            .bodyJson()
            .hasPath("$.registeredAt")
            .hasPathSatisfying("$.airHumidity",
                path -> assertThat(path).asNumber().isEqualTo(airHumidity));
    }

    @Test
    @DisplayName("create returns 400 BAD REQUEST for creating invalid humidity model.")
    void givenInvalidHumidityDtoModel_whenCreating_thenReturn400BadRequest() {
        LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double airHumidity = 150.03;
        HumidityDto invalidHumidityDto = HumidityDto.builder()
                                                    .registeredAt(registeredAt)
                                                    .airHumidity(airHumidity)
                                                    .build();
        String requestJson = objectMapper.writeValueAsString(invalidHumidityDto);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/api/humidities")
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
            .hasPathSatisfying("$.errors.airHumidity",
                path -> assertThat(path).asString().isNotBlank());
    }

    @Test
    @DisplayName("deleteHumidityById returns 204 NO CONTENT with valid id.")
    void givenValidId_whenDeletingHumidityById_thenReturn204NoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(humidityService).deleteHumidityById(id);

        MvcTestResult result = mockMvcTester.delete()
                                            .header("X-API-KEY", "api-key-secret")
                                            .uri("/api/humidities/{id}", id)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteHumidityById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenDeletingHumidityById_thenReturn404NotFound() {
        UUID invalidId = new UUID(0, 0);
        doThrow(new ResourceNotFoundException("Humidity with id=" + invalidId + " not found."))
            .when(humidityService).deleteHumidityById(invalidId);

        MvcTestResult result = mockMvcTester.delete()
                                            .header("X-API-KEY", "api-key-secret")
                                            .uri("/api/humidities/{id}", invalidId)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Humidity with id=" + invalidId + " not found.");
    }

    @Test
    @DisplayName("deleteHumiditiesByDateOrTimestamp returns 204 NO CONTENT with valid dateTime.")
    void givenValidDateTime_whenDeletingHumiditiesByDateOrTimestamp_thenReturn204NoContent() {
        boolean checkOnlyDate = false;
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doNothing().when(humidityService).deleteHumiditiesByDateOrTimestamp(dateTime, checkOnlyDate);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/humidities")
                                            .header("X-API-KEY", "api-key-secret")
                                            .param("dateTime", dateTime.toString())
                                            .param("checkOnlyDate", String.valueOf(checkOnlyDate))
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteHumiditiesByDateOrTimestamp returns 404 NOT FOUND with invalid dateTime.")
    void givenInvalidDateTime_whenDeletingHumiditiesByDateOrTimestamp_thenReturn404NotFound() {
        boolean checkOnlyDate = false;
        LocalDateTime invalidDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doThrow(new ResourceNotFoundException("Humidities with dateTime=" + invalidDateTime + " not found."))
            .when(humidityService).deleteHumiditiesByDateOrTimestamp(invalidDateTime, checkOnlyDate);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/api/humidities")
                                            .header("X-API-KEY", "api-key-secret")
                                            .param("dateTime", invalidDateTime.toString())
                                            .param("checkOnlyDate", String.valueOf(checkOnlyDate))
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Humidities with dateTime=" + invalidDateTime + " not found.");
    }
}
