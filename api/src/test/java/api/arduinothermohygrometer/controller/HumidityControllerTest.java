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
import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.service.HumidityService;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@DisplayName("HumidityController MVC slice unit tests.")
@WebMvcTest(HumidityController.class)
class HumidityControllerTest extends WebMvcTestBase {
    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HumidityService humidityService;

    @Nested
    class GetMethods {
        @Test
        void givenValidId_whenGetHumidityById_thenReturn200OK() {
            UUID id = UUID.randomUUID();
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Double airHumidity = 20.01;
            HumidityDto humidityDto = HumidityDto.builder()
                                                 .registeredAt(registeredAt)
                                                 .airHumidity(airHumidity)
                                                 .build();
            when(humidityService.getHumidityById(id)).thenReturn(humidityDto);

            MvcTestResult result = mockMvcTester.get()
                                                .uri("/api/v1/humidities/{id}", id)
                                                .exchange();

            assertThat(result)
                .hasStatusOk()
                .bodyJson()
                .hasPath("$.registeredAt")
                .hasPathSatisfying("$.airHumidity",
                    path -> assertThat(path).asNumber().isEqualTo(airHumidity));
        }

        @Test
        void givenInvalidId_whenGetHumidityById_thenReturn404NotFound() {
            UUID invalidId = new UUID(0, 0);
            when(humidityService.getHumidityById(invalidId))
                .thenThrow(new ResourceNotFoundException("Humidity with id=" + invalidId + " not found."));

            MvcTestResult result = mockMvcTester.get()
                                                .uri("/api/v1/humidities/{id}", invalidId)
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .failure()
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Humidity with id=" + invalidId + " not found.");
        }

        @Test
        void givenValidRegisteredAt_whenGetHumiditiesByDateOrTimestamp_thenReturn200OK() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Double airHumidity = 20.01;
            Double airHumidity2 = 90.01;
            HumidityDto humidityDto = HumidityDto.builder()
                                                 .registeredAt(registeredAt)
                                                 .airHumidity(airHumidity)
                                                 .build();
            HumidityDto humidityDto2 = HumidityDto.builder()
                                                  .registeredAt(registeredAt.minusHours(1))
                                                  .airHumidity(airHumidity2)
                                                  .build();
            List<HumidityDto> humidities = List.of(humidityDto, humidityDto2);
            when(humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly)).thenReturn(humidities);

            MvcTestResult result = mockMvcTester.get()
                                                .uri("/api/v1/humidities")
                                                .param("registeredAt", registeredAt.toString())
                                                .param("dateOnly", String.valueOf(dateOnly))
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
        void givenInvalidRegisteredAt_whenGetHumiditiesByDateOrTimestamp_thenReturn404NotFound() {
            boolean dateOnly = true;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            when(humidityService.getHumiditiesByDateOrTimestamp(registeredAt, dateOnly))
                .thenThrow(new ResourceNotFoundException("Humidities registeredAt=" + registeredAt + " not found."));

            MvcTestResult result = mockMvcTester.get()
                                                .uri("/api/v1/humidities")
                                                .param("registeredAt", registeredAt.toString())
                                                .param("dateOnly", String.valueOf(dateOnly))
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .failure()
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Humidities registeredAt=" + registeredAt + " not found.");
        }
    }

    @Nested
    class CreateMethods {
        @Test
        void givenValidHumidityDtoModel_whenCreateHumidity_thenReturn201CREATED() {
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Double airHumidity = 21.02;
            HumidityDto humidityDto = HumidityDto.builder()
                                                 .registeredAt(registeredAt)
                                                 .airHumidity(airHumidity)
                                                 .build();
            when(humidityService.createHumidity(any())).thenReturn(humidityDto);
            String requestJson = objectMapper.writeValueAsString(humidityDto);

            MvcTestResult result = mockMvcTester.post()
                                                .uri("/api/v1/humidities")
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
        void givenInvalidHumidityDto_whenCreateHumidity_thenReturn400BadRequest() {
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            Double airHumidity = 150.03;
            HumidityDto invalidHumidityDto = HumidityDto.builder()
                                                        .registeredAt(registeredAt)
                                                        .airHumidity(airHumidity)
                                                        .build();
            String requestJson = objectMapper.writeValueAsString(invalidHumidityDto);

            MvcTestResult result = mockMvcTester.post()
                                                .uri("/api/v1/humidities")
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
                    path -> assertThat(path).asString().isEqualTo("airHumidity"));
        }
    }

    @Nested
    class DeleteMethods {
        @Test
        void givenValidId_whenDeleteHumidityById_thenReturn204NoContent() {
            UUID id = UUID.randomUUID();
            doNothing().when(humidityService).deleteHumidityById(id);

            MvcTestResult result = mockMvcTester.delete()
                                                .uri("/api/v1/humidities/{id}", id)
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NO_CONTENT);
        }

        @Test
        void givenInvalidId_whenDeleteHumidityById_thenReturn404NotFound() {
            UUID invalidId = new UUID(0, 0);
            doThrow(new ResourceNotFoundException("Humidity with id=" + invalidId + " not found."))
                .when(humidityService).deleteHumidityById(invalidId);

            MvcTestResult result = mockMvcTester.delete()
                                                .uri("/api/v1/humidities/{id}", invalidId)
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .failure()
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Humidity with id=" + invalidId + " not found.");
        }

        @Test
        void givenValidRegisteredAt_whenDeleteHumiditiesByDateOrTimestamp_thenReturn204NoContent() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            doNothing().when(humidityService).deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            MvcTestResult result = mockMvcTester.delete()
                                                .uri("/api/v1/humidities")
                                                .param("registeredAt", registeredAt.toString())
                                                .param("dateOnly", String.valueOf(dateOnly))
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NO_CONTENT);
        }

        @Test
        void givenInvalidRegisteredAt_whenDeleteHumiditiesByDateOrTimestamp_thenReturn404NotFound() {
            boolean dateOnly = false;
            LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
            doThrow(new ResourceNotFoundException("Humidities registeredAt=" + registeredAt + " not found."))
                .when(humidityService).deleteHumiditiesByDateOrTimestamp(registeredAt, dateOnly);

            MvcTestResult result = mockMvcTester.delete()
                                                .uri("/api/v1/humidities")
                                                .param("registeredAt", registeredAt.toString())
                                                .param("dateOnly", String.valueOf(dateOnly))
                                                .exchange();

            assertThat(result)
                .hasStatus(HttpStatus.NOT_FOUND)
                .failure()
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Humidities registeredAt=" + registeredAt + " not found.");
        }
    }
}
