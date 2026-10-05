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
import api.arduinothermohygrometer.dto.BatteryDto;
import api.arduinothermohygrometer.exception.ResourceNotFoundException;
import api.arduinothermohygrometer.mapper.BatteryDtoMapper;
import api.arduinothermohygrometer.model.Battery;
import api.arduinothermohygrometer.service.BatteryService;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(BatteryController.class)
class BatteryControllerTest extends WebMvcTestBase {
  @MockitoBean private BatteryService batteryService;

  @MockitoBean private BatteryDtoMapper batteryDtoMapper;

  @Autowired private MockMvcTester mockMvcTester;

  @Autowired private ObjectMapper objectMapper;

  ClassPathResource getBatteriesResponseJson =
      new ClassPathResource("testfiles/get_batteries_response.json");
  ClassPathResource createBatteryResponseJson =
      new ClassPathResource("testfiles/create_battery_response.json");

  @Nested
  class GetMethods {
    @Test
    void givenValidRegisteredAt_thenReturn200OK() {
      LocalDateTime registeredAt = LocalDateTime.parse("2026-06-01T12:00:00");
      List<Battery> batteries =
          List.of(new Battery(registeredAt, 95), new Battery(registeredAt.plusHours(1), 90));
      when(batteryDtoMapper.toDto(any(Battery.class))).thenCallRealMethod();
      when(batteryService.getBatteriesByDateOrTimestamp(registeredAt, true)).thenReturn(batteries);

      MvcTestResult result =
          mockMvcTester
              .get()
              .uri("/api/v1/batteries")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(true))
              .exchange();

      assertThat(result).hasStatusOk().bodyJson().isStrictlyEqualTo(getBatteriesResponseJson);
    }

    @Test
    void givenInvalidRegisteredAt_thenReturn404NotFound() {
      LocalDateTime registeredAt = LocalDateTime.parse("2026-06-01T12:00:00");
      when(batteryService.getBatteriesByDateOrTimestamp(registeredAt, true))
          .thenThrow(
              new ResourceNotFoundException(
                  "Batteries registeredAt=" + registeredAt + " not found."));

      MvcTestResult result =
          mockMvcTester
              .get()
              .uri("/api/v1/batteries")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(true))
              .exchange();

      assertThat(result)
          .hasStatus(HttpStatus.NOT_FOUND)
          .failure()
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Batteries registeredAt=" + registeredAt + " not found.");
    }
  }

  @Nested
  class CreateMethods {
    @Test
    void givenValidBatteryDto_thenReturn201Created() {
      Battery battery = new Battery(LocalDateTime.parse("2026-06-01T12:00:00"), 95);
      BatteryDto batteryDto =
          BatteryDto.builder()
              .registeredAt(battery.getRegisteredAt())
              .batteryStatus(battery.getBatteryStatus())
              .build();
      when(batteryDtoMapper.toModel(any(BatteryDto.class))).thenReturn(battery);
      when(batteryService.createBattery(any(Battery.class))).thenReturn(battery);
      when(batteryDtoMapper.toDto(any(Battery.class))).thenReturn(batteryDto);
      String requestJson = objectMapper.writeValueAsString(battery);

      MvcTestResult result =
          mockMvcTester
              .post()
              .uri("/api/v1/batteries")
              .contentType(MediaType.APPLICATION_JSON)
              .content(requestJson)
              .exchange();

      assertThat(result)
          .hasStatus(HttpStatus.CREATED)
          .bodyJson()
          .isStrictlyEqualTo(createBatteryResponseJson);
    }

    @Test
    void givenInvalidBatteryDto_thenReturn400BadRequest() {
      BatteryDto invalidBatteryDto =
          BatteryDto.builder()
              .registeredAt(LocalDateTime.parse("2026-06-01T12:00:00"))
              .batteryStatus(105)
              .build();
      String requestJson = objectMapper.writeValueAsString(invalidBatteryDto);

      MvcTestResult result =
          mockMvcTester
              .post()
              .uri("/api/v1/batteries")
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
              path -> assertThat(path).asString().isEqualTo("batteryStatus"));
    }
  }

  @Nested
  class DeleteMethods {
    @Test
    void givenValidRegisteredAt_thenReturn204NoContent() {
      LocalDateTime registeredAt = LocalDateTime.parse("2026-06-01T12:00:00");
      doNothing().when(batteryService).deleteBatteriesByDateOrTimestamp(registeredAt, false);

      MvcTestResult result =
          mockMvcTester
              .delete()
              .uri("/api/v1/batteries")
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
                  "Batteries registeredAt=" + registeredAt + " not found."))
          .when(batteryService)
          .deleteBatteriesByDateOrTimestamp(registeredAt, false);

      MvcTestResult result =
          mockMvcTester
              .delete()
              .uri("/api/v1/batteries")
              .param("registeredAt", registeredAt.toString())
              .param("dateOnly", String.valueOf(false))
              .exchange();

      assertThat(result)
          .hasStatus(HttpStatus.NOT_FOUND)
          .failure()
          .isInstanceOf(ResourceNotFoundException.class)
          .hasMessage("Batteries registeredAt=" + registeredAt + " not found.");
    }
  }
}
