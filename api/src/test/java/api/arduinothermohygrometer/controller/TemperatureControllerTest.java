package api.arduinothermohygrometer.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
@DisplayName("TemperatureController MVC slice unit tests.")
@WebMvcTest(TemperatureController.class)
class TemperatureControllerTest extends WebMvcTestBase {
    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TemperatureService temperatureService;

    @Nested
    class GetMethods {
        @Test
        void givenValidId_whenGetTemperatureById_thenReturn200OK() {
            UUID id = UUID.randomUUID();
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Double temp = 20.01;
            TemperatureDto temperatureDto = TemperatureDto.builder()
                                                          .registeredAt(registeredAt)
                                                          .temp(temp)
                                                          .build();
            when(temperatureService.getTemperatureById(id)).thenReturn(temperatureDto);

            MvcTestResult result = mockMvcTester.get()
                                                .uri("/api/v1/temperatures/{id}", id)
                                                .exchange();

            assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPath("$.registeredAt")
                .hasPathSatisfying("$.temp",
                    path -> assertThat(path).asNumber().isEqualTo(temp));
        }

        @Test
        void givenInvalidId_whenGetTemperatureById_thenReturn404NotFound() {
            UUID invalidId = new UUID(0, 0);
            when(temperatureService.getTemperatureById(invalidId))
                .thenThrow(new ResourceNotFoundException("Temperature with id=" + invalidId + " not found."));

            MvcTestResult result = mockMvcTester.get()
                                                .uri("/api/v1/temperatures/{id}", invalidId)
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .failure()
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Temperature with id=" + invalidId + " not found.");
        }

        @Test
        void givenValidRegisteredAt_whenGetTemperaturesByDateOrTimestamp_thenReturn200OK() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Double temp = 20.01;
            Double temp2 = 90.01;
            TemperatureDto temperatureDto = TemperatureDto.builder()
                                                          .registeredAt(registeredAt)
                                                          .temp(temp)
                                                          .build();
            TemperatureDto temperatureDto2 = TemperatureDto.builder()
                                                           .registeredAt(registeredAt.minusHours(1))
                                                           .temp(temp2)
                                                           .build();
            List<TemperatureDto> temperatureDtos = List.of(temperatureDto, temperatureDto2);
            when(temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly)).thenReturn(temperatureDtos);

            MvcTestResult result = mockMvcTester.get()
                                                .uri("/api/v1/temperatures")
                                                .param("registeredAt", registeredAt.toString())
                                                .param("dateOnly", String.valueOf(dateOnly))
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
        void givenInvalidRegisteredAt_whenGetTemperaturesByDateOrTimestamp_thenReturn404NotFound() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            when(temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly))
                .thenThrow(new ResourceNotFoundException("Temperatures registeredAt=" + registeredAt + " not found."));

            MvcTestResult result = mockMvcTester.get()
                                                .uri("/api/v1/temperatures")
                                                .param("registeredAt", registeredAt.toString())
                                                .param("dateOnly", String.valueOf(dateOnly))
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .failure()
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Temperatures registeredAt=" + registeredAt + " not found.");
        }
    }

    @Nested
    class CreateMethods {
        @Test
        void givenValidTemperatureDtoModel_whenCreateTemperature_thenReturn201CREATED() {
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Double temp = 21.02;
            TemperatureDto temperatureDto = TemperatureDto.builder()
                                                          .registeredAt(registeredAt)
                                                          .temp(temp)
                                                          .build();
            when(temperatureService.createTemperature(any())).thenReturn(temperatureDto);
            String requestJson = objectMapper.writeValueAsString(temperatureDto);

            MvcTestResult result = mockMvcTester.post()
                                                .uri("/api/v1/temperatures")
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
        void givenInvalidTemperatureDto_whenCreateTemperature_thenReturn400BadRequest() {
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Double temp = 150.03;
            TemperatureDto invalidTemperatureDto = TemperatureDto.builder()
                                                                 .registeredAt(registeredAt)
                                                                 .temp(temp)
                                                                 .build();
            String requestJson = objectMapper.writeValueAsString(invalidTemperatureDto);

            MvcTestResult result = mockMvcTester.post()
                                                .uri("/api/v1/temperatures")
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
                .hasPathSatisfying("$.errors.[0].parameter",
                    path -> assertThat(path).asString().isEqualTo("temp"));
        }
    }

    @Nested
    class DeleteMethods {
        @Test
        void givenValidId_whenDeleteTemperatureById_thenReturn204NoContent() {
            UUID id = UUID.randomUUID();
            doNothing().when(temperatureService).deleteTemperatureById(id);

            MvcTestResult result = mockMvcTester.delete()
                                                .uri("/api/v1/temperatures/{id}", id)
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NO_CONTENT);
        }

        @Test
        void givenInvalidId_whenDeleteTemperatureById_thenReturn404NotFound() {
            UUID invalidId = new UUID(0, 0);
            doThrow(new ResourceNotFoundException("Temperature with id=" + invalidId + " not found.")).when(temperatureService)
                                                                                                      .deleteTemperatureById(invalidId);

            MvcTestResult result = mockMvcTester.delete()
                                                .uri("/api/v1/temperatures/{id}", invalidId)
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .failure()
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Temperature with id=" + invalidId + " not found.");
        }

        @Test
        void givenValidRegisteredAt_whenDeleteTemperaturesByDateOrTimestamp_thenReturn204NoContent() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            doNothing().when(temperatureService).deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            MvcTestResult result = mockMvcTester.delete()
                                                .uri("/api/v1/temperatures")
                                                .param("registeredAt", registeredAt.toString())
                                                .param("dateOnly", String.valueOf(dateOnly))
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NO_CONTENT);
        }

        @Test
        void givenInvalidRegisteredAt_whenDeleteTemperaturesByDateOrTimestamp_thenReturn404NotFound() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            doThrow(new ResourceNotFoundException("Temperatures registeredAt=" + registeredAt + " not found."))
                .when(temperatureService).deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

            MvcTestResult result = mockMvcTester.delete()
                                                .uri("/api/v1/temperatures")
                                                .param("registeredAt", registeredAt.toString())
                                                .param("dateOnly", String.valueOf(dateOnly))
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .failure()
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Temperatures registeredAt=" + registeredAt + " not found.");
        }
    }
}
