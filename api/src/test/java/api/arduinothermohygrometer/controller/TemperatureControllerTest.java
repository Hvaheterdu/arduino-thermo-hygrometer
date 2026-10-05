package api.arduinothermohygrometer.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

import api.arduinothermohygrometer.base.WebMvcTestBase;
import api.arduinothermohygrometer.dto.TemperatureDto;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.TemperatureDtoMapper;
import api.arduinothermohygrometer.model.Temperature;
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

  @MockitoBean private TemperatureDtoMapper temperatureDtoMapper;

  @Autowired private MockMvcTester mockMvcTester;

  @Autowired private ObjectMapper objectMapper;

  ClassPathResource getTemperaturesResponseJson =
      new ClassPathResource("testfiles/get_temperatures_response.json");
  ClassPathResource createTemperatureResponseJson =
      new ClassPathResource("testfiles/create_temperature_response.json");

  @Nested
  class GetMethods {
    @Test
    void givenValidRegisteredAt_thenReturn200OK() {
      LocalDateTime registeredAt = LocalDateTime.parse("2026-06-01T12:00:00");
      List<Temperature> temperatures =
          List.of(
              new Temperature(registeredAt, 20.01),
              new Temperature(registeredAt.plusHours(1), 90.01));
      when(temperatureDtoMapper.toDto(any(Temperature.class))).thenCallRealMethod();
      when(temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, true))
          .thenReturn(temperatures);

      MvcTestResult result =
          mockMvcTester
              .get()
              .uri("/api/v1/temperatures")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(true))
              .exchange();

      assertThat(result).hasStatusOk().bodyJson().isStrictlyEqualTo(getTemperaturesResponseJson);
    }

    @Test
    void givenInvalidRegisteredAt_thenReturn404NotFound() {
      LocalDateTime registeredAt = LocalDateTime.parse("2026-06-01T12:00:00");
      when(temperatureService.getTemperaturesByDateOrTimestamp(registeredAt, true))
          .thenThrow(
              new ResourceNotFoundException(
                  "Temperatures registeredAt=" + registeredAt + " not found."));

      MvcTestResult result =
          mockMvcTester
              .get()
              .uri("/api/v1/temperatures")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(true))
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
    void givenValidTemperatureDto_thenReturn201Created() {
      Temperature temperature = new Temperature(LocalDateTime.parse("2026-06-01T12:00:00"), 21.01);
      TemperatureDto temperatureDto =
          TemperatureDto.builder()
              .registeredAt(temperature.getRegisteredAt())
              .temp(temperature.getTemp())
              .build();
      when(temperatureDtoMapper.toModel(any(TemperatureDto.class))).thenReturn(temperature);
      when(temperatureService.createTemperature(any(Temperature.class))).thenReturn(temperature);
      when(temperatureDtoMapper.toDto(any(Temperature.class))).thenReturn(temperatureDto);
      String requestJson = objectMapper.writeValueAsString(temperature);

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
          .isStrictlyEqualTo(createTemperatureResponseJson);
    }

    @Test
    void givenInvalidTemperatureDto_thenReturn400BadRequest() {
      TemperatureDto invalidTemperatureDto =
          TemperatureDto.builder()
              .registeredAt(LocalDateTime.parse("2026-06-01T12:00:00"))
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
      LocalDateTime registeredAt = LocalDateTime.parse("2026-06-01T12:00:00");
      doNothing().when(temperatureService).deleteTemperaturesByDateOrTimestamp(registeredAt, false);

      MvcTestResult result =
          mockMvcTester
              .delete()
              .uri("/api/v1/temperatures")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(false))
              .exchange();

      assertThat(result).hasStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    void givenInvalidRegisteredAt_thenReturn404NotFound() {
      LocalDateTime registeredAt = LocalDateTime.parse("2026-06-01T12:00:00");
      doThrow(
              new ResourceNotFoundException(
                  "Temperatures registeredAt=" + registeredAt + " not found."))
          .when(temperatureService)
          .deleteTemperaturesByDateOrTimestamp(registeredAt, false);

      MvcTestResult result =
          mockMvcTester
              .delete()
              .uri("/api/v1/temperatures")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(false))
              .exchange();

      assertThat(result)
          .hasStatus(HttpStatus.NOT_FOUND)
          .failure()
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Temperatures registeredAt=" + registeredAt + " not found.");
    }
  }
}
