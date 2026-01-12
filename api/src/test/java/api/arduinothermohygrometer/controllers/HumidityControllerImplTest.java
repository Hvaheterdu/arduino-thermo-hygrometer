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

import api.arduinothermohygrometer.controllers.implementations.HumidityControllerImpl;
import api.arduinothermohygrometer.dtos.HumidityDto;
import api.arduinothermohygrometer.exceptions.GlobalExceptionHandler;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.services.HumidityService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for HumidityControllerImpl")
@ExtendWith(MockitoExtension.class)
class HumidityControllerImplTest {
    @Mock
    private HumidityService humidityService;

    private MockMvcTester mockMvcTester;

    @BeforeEach
    void setup() {
        mockMvcTester = MockMvcTester.of(new HumidityControllerImpl(humidityService), new GlobalExceptionHandler());
    }

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
        when(humidityService.getHumidityDtoById(id)).thenReturn(humidityDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/humidities/id/{id}", id)
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.registeredAt",
                path -> assertThat(path).asString().isEqualTo(registeredAt.toString()))
            .hasPathSatisfying("$.airHumidity",
                path -> assertThat(path).asNumber().isEqualTo(airHumidity));
    }

    @Test
    @DisplayName("getHumidityById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenGettingHumidityById_thenReturn404NotFound() {
        UUID id = new UUID(0, 0);
        when(humidityService.getHumidityDtoById(id))
            .thenThrow(new ResourceNotFoundException("Humidity with id=" + id + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/humidities/id/{id}", id)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Humidity with id=" + id + " not found.");
    }

    @Test
    @DisplayName("getHumidityByTimestamp returns 200 OK with valid timestamp.")
    void givenValidTimestamp_whenGettingHumidityByTimestamp_thenReturn200OK() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double airHumidity = 20.01;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(timestamp)
                                             .airHumidity(airHumidity)
                                             .build();
        when(humidityService.getHumidityDtoByTimestamp(timestamp)).thenReturn(humidityDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/humidities/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.registeredAt",
                path -> assertThat(path).asString().isEqualTo(timestamp.toString()))
            .hasPathSatisfying("$.airHumidity",
                path -> assertThat(path).asNumber().isEqualTo(airHumidity));
    }

    @Test
    @DisplayName("getHumidityByTimestamp returns 404 NOT FOUND with invalid timestamp.")
    void givenInvalidTimestamp_whenGettingHumidityByTimestamp_thenReturn404NotFound() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        when(humidityService.getHumidityDtoByTimestamp(timestamp))
            .thenThrow(new ResourceNotFoundException("Humidity with timestamp=" + timestamp + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/humidities/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Humidity with timestamp=" + timestamp + " not found.");
    }

    @Test
    @DisplayName("getHumidityByDate returns 200 OK with valid date.")
    void givenValidDate_whenGettingHumidityByDate_thenReturn200OK() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double airHumidity = 20.01;
        Double airHumidity2 = 90.01;
        HumidityDto humidityDto = HumidityDto.builder()
                                             .registeredAt(timestamp)
                                             .airHumidity(airHumidity)
                                             .build();
        HumidityDto humidityDto2 = HumidityDto.builder()
                                              .registeredAt(timestamp.minusHours(1))
                                              .airHumidity(airHumidity2)
                                              .build();
        List<HumidityDto> humidities = List.of(humidityDto, humidityDto2);
        when(humidityService.getHumidityDtosByDate(timestamp.toLocalDate())).thenReturn(humidities);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/humidities/date")
                                            .param("date", timestamp.toLocalDate().toString())
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
    @DisplayName("getHumidityByDate returns 404 NOT FOUND with invalid date.")
    void givenInvalidDate_whenGettingHumidityByDate_thenReturn404NotFound() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        when(humidityService.getHumidityDtosByDate(timestamp.toLocalDate()))
            .thenThrow(new ResourceNotFoundException("Humidities with date=" + timestamp.toLocalDate() + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/humidities/date")
                                            .param("date", timestamp.toLocalDate().toString())
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Humidities with date=" + timestamp.toLocalDate() + " not found.");
    }

    @Test
    @DisplayName("create returns 201 CREATED for creating valid humidity model.")
    void givenValidHumidityDtoModel_whenCreating_thenReturn200OK() {
        UUID id = UUID.randomUUID();
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double airHumidity = 21.02;
        String requestHumidityDto = String.format("""
            {
            "registeredAt": "%s",
            "airHumidity": %s
            }
            """, localDateTime, airHumidity);
        String responseHumidityDto = String.format("""
            {
            "registeredAt": "%s",
            "airHumidity": %s
            }
            """, localDateTime, airHumidity);
        HumidityDto humidityDto = HumidityDto.builder()
                                             .id(id)
                                             .registeredAt(localDateTime)
                                             .airHumidity(airHumidity)
                                             .build();
        when(humidityService.createHumidityDto(any())).thenReturn(humidityDto);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/v1/api/humidities/create")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestHumidityDto)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.CREATED)
            .bodyJson()
            .isEqualTo(responseHumidityDto);
    }

    @Test
    @DisplayName("create returns 400 BAD REQUEST for creating invalid humidity model.")
    void givenInvalidHumidityDtoModel_whenCreating_thenReturn400BadRequest() {
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double airHumidity = 150.03;
        String requestHumidityDto = String.format("""
            {
            "registeredAt": "%s",
            "airHumidity": %s
            }
            """, localDateTime, airHumidity);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/v1/api/humidities/create")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestHumidityDto)
                                            .exchange();

        verifyNoInteractions(humidityService);
        assertThat(result)
            .hasStatus(HttpStatus.BAD_REQUEST)
            .bodyJson()
            .hasPathSatisfying("$.detail",
                path -> assertThat(path).asString().isEqualTo("One or more fields are invalid."))
            .hasPathSatisfying("$.title",
                path -> assertThat(path).asString().isEqualTo("Model validation error."))
            .hasPathSatisfying("$.errors.airHumidity",
                path -> assertThat(path).asString().isNotBlank());
    }

    @Test
    @DisplayName("deleteHumidityById returns 204 NO CONTENT with valid id.")
    void givenValidId_whenDeletingHumidityById_thenReturn204NoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(humidityService).deleteHumidityById(id);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/humidities/delete/id/{id}", id)
                                            .exchange();

        verify(humidityService, times(1)).deleteHumidityById(id);
        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteHumidityById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenDeletingHumidityById_thenReturn404NotFound() {
        UUID id = new UUID(0, 0);
        doThrow(new ResourceNotFoundException("Humidity with id=" + id + " not found.")).when(humidityService)
                                                                                        .deleteHumidityById(id);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/humidities/delete/id/{id}", id)
                                            .exchange();

        verify(humidityService, times(1)).deleteHumidityById(id);
        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Humidity with id=" + id + " not found.");
    }

    @Test
    @DisplayName("deleteHumidityByTimestamp returns 204 NO CONTENT with valid timestamp.")
    void givenValidTimestamp_whenDeletingHumidityByTimestamp_thenReturn204NoContent() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doNothing().when(humidityService).deleteHumidityByTimestamp(timestamp);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/humidities/delete/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        verify(humidityService, times(1)).deleteHumidityByTimestamp(timestamp);
        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteHumidityByTimestamp returns 404 NOT FOUND with invalid timestamp.")
    void givenInvalidTimestamp_whenDeletingHumidityByTimestamp_thenReturn404NotFound() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doThrow(new ResourceNotFoundException(
            "Humidity with timestamp=" + timestamp + " not found.")).when(humidityService)
                                                                    .deleteHumidityByTimestamp(timestamp);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/humidities/delete/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        verify(humidityService, times(1)).deleteHumidityByTimestamp(timestamp);
        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Humidity with timestamp=" + timestamp + " not found.");
    }
}
