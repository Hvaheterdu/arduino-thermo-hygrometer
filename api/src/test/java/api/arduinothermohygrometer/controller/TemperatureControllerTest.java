package api.arduinothermohygrometer.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
@WebMvcTest(TemperatureController.class)
class TemperatureControllerTest extends WebMvcTestBase {
  @MockitoBean private TemperatureService temperatureService;

  @Autowired private MockMvcTester mockMvcTester;

  @Autowired private ObjectMapper objectMapper;

  @Nested
  class GetMethods {
    @Test
    void givenValidRegisteredAt_thenReturn200OK() {
      boolean dateOnly = true;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      List<TemperatureDto> temperatureDtos =
          List.of(
              TemperatureDto.builder().registeredAt(registeredAt).temp(20.01).build(),
              TemperatureDto.builder()
                  .registeredAt(registeredAt.minusHours(1))
                  .temp(90.01)
                  .build());
      when(temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly))
          .thenReturn(temperatureDtos);

      MvcTestResult result =
          mockMvcTester
              .get()
              .uri("/api/v1/temperatures")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(dateOnly))
              .exchange();

      assertThat(result)
          .hasStatusOk()
          .bodyJson()
          .hasPathSatisfying("$.[0].temp", path -> assertThat(path).asNumber().isEqualTo(20.01))
          .hasPathSatisfying("$.[1].temp", path -> assertThat(path).asNumber().isEqualTo(90.01));
    }

    @Test
    void givenInvalidRegisteredAt_thenReturn404NotFound() {
      boolean dateOnly = true;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      when(temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, dateOnly))
          .thenThrow(
              new ResourceNotFoundException(
                  "Temperatures registeredAt=" + registeredAt + " not found."));

      MvcTestResult result =
          mockMvcTester
              .get()
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
    void givenValidTemperatureDtoModel_thenReturn201CREATED() {
      TemperatureDto temperatureDto =
          TemperatureDto.builder()
              .registeredAt(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
              .temp(21.01)
              .build();
      when(temperatureService.createTemperature(any(TemperatureDto.class)))
          .thenReturn(temperatureDto);
      String requestJson = objectMapper.writeValueAsString(temperatureDto);

      MvcTestResult result =
          mockMvcTester
              .post()
              .uri("/api/v1/temperatures")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestJson)
              .exchange();

      assertThat(result)
          .hasStatus(HttpStatus.CREATED)
          .bodyJson()
          .hasPath("$.registeredAt")
          .hasPathSatisfying("$.temp", path -> assertThat(path).asNumber().isEqualTo(21.01));
    }

    @Test
    void givenInvalidTemperatureDto_thenReturn400BadRequest() {
      TemperatureDto invalidTemperatureDto =
          TemperatureDto.builder()
              .registeredAt(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
              .temp(150.03)
              .build();
      String requestJson = objectMapper.writeValueAsString(invalidTemperatureDto);

      MvcTestResult result =
          mockMvcTester
              .post()
              .uri("/api/v1/temperatures")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestJson)
              .exchange();

      assertThat(result)
          .hasStatus(HttpStatus.BAD_REQUEST)
          .bodyJson()
          .hasPathSatisfying(
              "$.detail",
              path -> assertThat(path).asString().isEqualTo("One or more fields are invalid."))
          .hasPathSatisfying(
              "$.title", path -> assertThat(path).asString().isEqualTo("Entity validation error."))
          .hasPathSatisfying(
              "$.errors.[0].parameter", path -> assertThat(path).asString().isEqualTo("temp"));
    }
  }

  @Nested
  class DeleteMethods {
    @Test
    void givenValidRegisteredAt_thenReturn204NoContent() {
      boolean dateOnly = false;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      doNothing()
          .when(temperatureService)
          .deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

      MvcTestResult result =
          mockMvcTester
              .delete()
              .uri("/api/v1/temperatures")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(dateOnly))
              .exchange();

      assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    void givenInvalidRegisteredAt_thenReturn404NotFound() {
      boolean dateOnly = false;
      LocalDateTime registeredAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
      doThrow(
              new ResourceNotFoundException(
                  "Temperatures registeredAt=" + registeredAt + " not found."))
          .when(temperatureService)
          .deleteTemperaturesByDateOrTimestamp(registeredAt, dateOnly);

      MvcTestResult result =
          mockMvcTester
              .delete()
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
