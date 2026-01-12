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

import api.arduinothermohygrometer.controllers.implementations.TemperatureControllerImpl;
import api.arduinothermohygrometer.dtos.TemperatureDto;
import api.arduinothermohygrometer.exceptions.GlobalExceptionHandler;
import api.arduinothermohygrometer.exceptions.ResourceNotFoundException;
import api.arduinothermohygrometer.services.TemperatureService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("Unit tests for TemperatureControllerImpl")
@ExtendWith(MockitoExtension.class)
class TemperatureControllerImplTest {
    @Mock
    private TemperatureService temperatureService;

    private MockMvcTester mockMvcTester;

    @BeforeEach
    void setup() {
        mockMvcTester = MockMvcTester.of(new TemperatureControllerImpl(temperatureService), new GlobalExceptionHandler());
    }

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
        when(temperatureService.getTemperatureDtoById(id)).thenReturn(temperatureDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/temperatures/id/{id}", id)
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.registeredAt",
                path -> assertThat(path).asString().isEqualTo(registeredAt.toString()))
            .hasPathSatisfying("$.temp",
                path -> assertThat(path).asNumber().isEqualTo(temp));
    }

    @Test
    @DisplayName("getTemperatureById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenGettingTemperatureById_thenReturn404NotFound() {
        UUID id = new UUID(0, 0);
        when(temperatureService.getTemperatureDtoById(id))
            .thenThrow(new ResourceNotFoundException("Temperature with id=" + id + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/temperatures/id/{id}", id)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Temperature with id=" + id + " not found.");
    }

    @Test
    @DisplayName("getTemperatureByTimestamp returns 200 OK with valid timestamp.")
    void givenValidTimestamp_whenGettingTemperatureByTimestamp_thenReturn200OK() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double temp = 20.01;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(timestamp)
                                                      .temp(temp)
                                                      .build();
        when(temperatureService.getTemperatureDtoByTimestamp(timestamp)).thenReturn(temperatureDto);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/temperatures/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        assertThat(result)
            .hasStatusOk()
            .bodyJson()
            .hasPathSatisfying("$.registeredAt",
                path -> assertThat(path).asString().isEqualTo(timestamp.toString()))
            .hasPathSatisfying("$.temp",
                path -> assertThat(path).asNumber().isEqualTo(temp));
    }

    @Test
    @DisplayName("getTemperatureByTimestamp returns 404 NOT FOUND with invalid timestamp.")
    void givenInvalidTimestamp_whenGettingTemperatureByTimestamp_thenReturn404NotFound() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        when(temperatureService.getTemperatureDtoByTimestamp(timestamp))
            .thenThrow(new ResourceNotFoundException("Temperature with timestamp=" + timestamp + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/temperatures/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Temperature with timestamp=" + timestamp + " not found.");
    }

    @Test
    @DisplayName("getTemperatureByDate returns 200 OK with valid date.")
    void givenValidDate_whenGettingTemperatureByDate_thenReturn200OK() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double temp = 20.01;
        Double temp2 = 90.01;
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .registeredAt(timestamp)
                                                      .temp(temp)
                                                      .build();
        TemperatureDto temperatureDto2 = TemperatureDto.builder()
                                                       .registeredAt(timestamp.minusHours(1))
                                                       .temp(temp2)
                                                       .build();
        List<TemperatureDto> temperatures = List.of(temperatureDto, temperatureDto2);
        when(temperatureService.getTemperatureDtosByDate(timestamp.toLocalDate())).thenReturn(temperatures);

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/temperatures/date")
                                            .param("date", timestamp.toLocalDate().toString())
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
    @DisplayName("getTemperatureByDate returns 404 NOT FOUND with invalid date.")
    void givenInvalidDate_whenGettingTemperatureByDate_thenReturn404NotFound() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        when(temperatureService.getTemperatureDtosByDate(timestamp.toLocalDate()))
            .thenThrow(new ResourceNotFoundException("Temperatures with date=" + timestamp.toLocalDate() + " not found."));

        MvcTestResult result = mockMvcTester.get()
                                            .uri("/v1/api/temperatures/date")
                                            .param("date", timestamp.toLocalDate().toString())
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Temperatures with date=" + timestamp.toLocalDate() + " not found.");
    }

    @Test
    @DisplayName("create returns 201 CREATED for creating valid temperature model.")
    void givenValidTemperatureDtoModel_whenCreating_thenReturn200OK() {
        UUID id = UUID.randomUUID();
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double temp = 21.02;
        String requestTemperatureDto = String.format("""
            {
            "registeredAt": "%s",
            "temp": %s
            }
            """, localDateTime, temp);
        String responseTemperatureDto = String.format("""
            {
            "registeredAt": "%s",
            "temp": %s
            }
            """, localDateTime, temp);
        TemperatureDto temperatureDto = TemperatureDto.builder()
                                                      .id(id)
                                                      .registeredAt(localDateTime)
                                                      .temp(temp)
                                                      .build();
        when(temperatureService.createTemperatureDto(any())).thenReturn(temperatureDto);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/v1/api/temperatures/create")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestTemperatureDto)
                                            .exchange();

        assertThat(result)
            .hasStatus(HttpStatus.CREATED)
            .bodyJson()
            .isEqualTo(responseTemperatureDto);
    }

    @Test
    @DisplayName("create returns 400 BAD REQUEST for creating invalid temperature model.")
    void givenInvalidTemperatureDtoModel_whenCreating_thenReturn400BadRequest() {
        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        Double temp = 150.03;
        String requestTemperatureDto = String.format("""
            {
            "registeredAt": "%s",
            "temp": %s
            }
            """, localDateTime, temp);

        MvcTestResult result = mockMvcTester.post()
                                            .uri("/v1/api/temperatures/create")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .content(requestTemperatureDto)
                                            .exchange();

        verifyNoInteractions(temperatureService);
        assertThat(result)
            .hasStatus(HttpStatus.BAD_REQUEST)
            .bodyJson()
            .hasPathSatisfying("$.detail",
                path -> assertThat(path).asString().isEqualTo("One or more fields are invalid."))
            .hasPathSatisfying("$.title",
                path -> assertThat(path).asString().isEqualTo("Model validation error."))
            .hasPathSatisfying("$.errors.temp",
                path -> assertThat(path).asString().isNotBlank());
    }

    @Test
    @DisplayName("deleteTemperatureById returns 204 NO CONTENT with valid id.")
    void givenValidId_whenDeletingTemperatureById_thenReturn204NoContent() {
        UUID id = UUID.randomUUID();
        doNothing().when(temperatureService).deleteTemperatureById(id);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/temperatures/delete/id/{id}", id)
                                            .exchange();

        verify(temperatureService, times(1)).deleteTemperatureById(id);
        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteTemperatureById returns 404 NOT FOUND with invalid id.")
    void givenInvalidId_whenDeletingTemperatureById_thenReturn404NotFound() {
        UUID id = new UUID(0, 0);
        doThrow(new ResourceNotFoundException("Temperature with id=" + id + " not found.")).when(temperatureService)
                                                                                           .deleteTemperatureById(id);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/temperatures/delete/id/{id}", id)
                                            .exchange();

        verify(temperatureService, times(1)).deleteTemperatureById(id);
        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Temperature with id=" + id + " not found.");
    }

    @Test
    @DisplayName("deleteTemperatureByTimestamp returns 204 NO CONTENT with valid timestamp.")
    void givenValidTimestamp_whenDeletingTemperatureByTimestamp_thenReturn204NoContent() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doNothing().when(temperatureService).deleteTemperatureByTimestamp(timestamp);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/temperatures/delete/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        verify(temperatureService, times(1)).deleteTemperatureByTimestamp(timestamp);
        assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("deleteTemperatureByTimestamp returns 404 NOT FOUND with invalid timestamp.")
    void givenInvalidTimestamp_whenDeletingTemperatureByTimestamp_thenReturn404NotFound() {
        LocalDateTime timestamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        doThrow(new ResourceNotFoundException(
            "Temperature with timestamp=" + timestamp + " not found.")).when(temperatureService)
                                                                       .deleteTemperatureByTimestamp(timestamp);

        MvcTestResult result = mockMvcTester.delete()
                                            .uri("/v1/api/temperatures/delete/timestamp")
                                            .param("timestamp", timestamp.toString())
                                            .exchange();

        verify(temperatureService, times(1)).deleteTemperatureByTimestamp(timestamp);
        assertThat(result)
            .hasStatus(HttpStatus.NOT_FOUND)
            .failure()
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("Temperature with timestamp=" + timestamp + " not found.");
    }
}
