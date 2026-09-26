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
import api.arduinothermohygrometer.dto.HumidityDto;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.HumidityDtoMapper;
import api.arduinothermohygrometer.model.Humidity;
import api.arduinothermohygrometer.service.HumidityService;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(HumidityController.class)
class HumidityControllerTest extends WebMvcTestBase {
  @MockitoBean private HumidityService humidityService;

  @MockitoBean private HumidityDtoMapper humidityDtoMapper;

  @Autowired private MockMvcTester mockMvcTester;

  @Autowired private ObjectMapper objectMapper;

  ClassPathResource getHumiditiesResponseJson =
      new ClassPathResource("testfiles/get_humidities_response.json");
  ClassPathResource createHumidityResponseJson =
      new ClassPathResource("testfiles/create_humidity_response.json");

  @Nested
  class GetMethods {
    @Test
    void givenValidRegisteredAt_thenReturn200OK() {
      LocalDateTime registeredAt = LocalDateTime.parse("2026-06-01T12:00:00");
      List<Humidity> humidities =
          List.of(
              new Humidity(registeredAt, 20.01), new Humidity(registeredAt.plusHours(1), 90.01));
      when(humidityDtoMapper.toDto(any(Humidity.class))).thenCallRealMethod();
      when(humidityService.getHumiditiesByDateOrTimestamp(registeredAt, true))
          .thenReturn(humidities);

      MvcTestResult result =
          mockMvcTester
              .get()
              .uri("/api/v1/humidities")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(true))
              .exchange();

      assertThat(result).hasStatusOk().bodyJson().isStrictlyEqualTo(getHumiditiesResponseJson);
    }

    @Test
    void givenInvalidRegisteredAt_thenReturn404NotFound() {
      LocalDateTime registeredAt = LocalDateTime.parse("2026-06-01T12:00:00");
      when(humidityService.getHumiditiesByDateOrTimestamp(registeredAt, true))
          .thenThrow(
              new ResourceNotFoundException(
                  "Humidities registeredAt=" + registeredAt + " not found."));

      MvcTestResult result =
          mockMvcTester
              .get()
              .uri("/api/v1/humidities")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(true))
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
    void givenValidHumidityDto_thenReturn201CREATED() {
      Humidity humidity = new Humidity(LocalDateTime.parse("2026-06-01T12:00:00"), 21.02);
      HumidityDto humidityDto =
          HumidityDto.builder()
              .registeredAt(humidity.getRegisteredAt())
              .airHumidity(humidity.getAirHumidity())
              .build();
      when(humidityDtoMapper.toModel(any(HumidityDto.class))).thenReturn(humidity);
      when(humidityService.createHumidity(any(Humidity.class))).thenReturn(humidity);
      when(humidityDtoMapper.toDto(any(Humidity.class))).thenReturn(humidityDto);
      String requestJson = objectMapper.writeValueAsString(humidity);

      MvcTestResult result =
          mockMvcTester
              .post()
              .uri("/api/v1/humidities")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestJson)
              .exchange();

      assertThat(result)
          .hasStatus(HttpStatus.CREATED)
          .bodyJson()
          .isStrictlyEqualTo(createHumidityResponseJson);
    }

    @Test
    void givenInvalidHumidityDto_thenReturn400BadRequest() {
      HumidityDto invalidHumidityDto =
          HumidityDto.builder()
              .registeredAt(LocalDateTime.parse("2026-06-01T12:00:00"))
              .airHumidity(150.03)
              .build();
      String requestJson = objectMapper.writeValueAsString(invalidHumidityDto);

      MvcTestResult result =
          mockMvcTester
              .post()
              .uri("/api/v1/humidities")
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
              "$.errors.[0].parameter",
              path -> assertThat(path).asString().isEqualTo("airHumidity"));
    }
  }

  @Nested
  class DeleteMethods {
    @Test
    void givenValidRegisteredAt_thenReturn204NoContent() {
      LocalDateTime registeredAt = LocalDateTime.parse("2026-06-01T12:00:00");
      doNothing().when(humidityService).deleteHumiditiesByDateOrTimestamp(registeredAt, false);

      MvcTestResult result =
          mockMvcTester
              .delete()
              .uri("/api/v1/humidities")
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
                  "Humidities registeredAt=" + registeredAt + " not found."))
          .when(humidityService)
          .deleteHumiditiesByDateOrTimestamp(registeredAt, false);

      MvcTestResult result =
          mockMvcTester
              .delete()
              .uri("/api/v1/humidities")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(false))
              .exchange();

      assertThat(result)
          .hasStatus(HttpStatus.NOT_FOUND)
          .failure()
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Humidities registeredAt=" + registeredAt + " not found.");
    }
  }
}
