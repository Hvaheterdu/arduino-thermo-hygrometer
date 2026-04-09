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
               .andExpect(header().string("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate"))
               .andExpect(header().string("Content-Security-Policy",
                   "connect-src 'self'; "
                       + "default-src 'self'; "
                       + "frame-ancestors 'none'; "
                       + "img-src 'self' data:; "
                       + "script-src 'self'; "
                       + "style-src 'self' 'unsafe-inline';"))
               .andExpect(header().string("Referrer-Policy", "no-referrer"));
    }

    @Test
    @DisplayName("Given actuator endpoint when accessed over HTTPS then apply HSTS header")
    void givenActuatorEndpoint_whenAccessedOverHttps_thenApplyHstsHeader() throws Exception {
        mockMvc.perform(get("/actuator/health").secure(true))
               .andExpect(status().isOk())
               .andExpect(header().string("Strict-Transport-Security", "max-age=31536000 ; includeSubDomains"));
    }

    @Test
    @DisplayName("Given non actuator endpoint when accessed then return 403 FORBIDDEN")
    void givenNonActuatorEndpoint_whenAccessed_thenReturn403Forbidden() throws Exception {
        mockMvc.perform(get("/random-endpoint"))
               .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Given OpenAPI docs endpoint when accessed then return 200 OK")
    void givenOpenApiDocsEndpoint_whenAccessed_thenReturn200OK() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
               .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Given Swagger UI endpoint when accessed then return 200 OK")
    void givenSwaggerUiEndpoint_whenAccessed_thenReturn200OK() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
               .andExpect(status().isOk());
    }
}
