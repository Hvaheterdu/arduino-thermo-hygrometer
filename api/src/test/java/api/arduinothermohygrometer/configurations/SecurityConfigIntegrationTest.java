package api.arduinothermohygrometer.configurations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Given actuator endpoint when accessed then return 200 OK")
    void givenActuatorEndpoint_whenAccessed_thenReturn200OK() throws Exception {
        mockMvc.perform(get("/actuator/health"))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Given actuator endpoint when accessed then apply security headers")
    void givenActuatorEndpoint_whenAccessed_thenApplySecurityHeaders() throws Exception {
        mockMvc.perform(get("/actuator/health"))
               .andExpect(status().isOk())
               .andExpect(header().string("X-Content-Type-Options", "nosniff"))
               .andExpect(header().string("X-Frame-Options", "DENY"))
               .andExpect(header().string("X-XSS-Protection", "0"))
               .andExpect(header().string("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate"))
               .andExpect(header().string("Content-Security-Policy",
                   "connect-src 'none'; default-src 'none'; frame-ancestors 'none'; img-src 'none'; script-src 'none'; style-src 'none';"))
               .andExpect(header().string("Referrer-Policy", "no-referrer"));
    }

    @Test
    @DisplayName("Given non actuator endpoint when accessed then return 404 NOT FOUND")
    void givenNonActuatorEndpoint_whenAccessed_thenReturn404NotFound() throws Exception {
        mockMvc.perform(get("/random-endpoint"))
               .andExpect(status().isNotFound());
    }
}
