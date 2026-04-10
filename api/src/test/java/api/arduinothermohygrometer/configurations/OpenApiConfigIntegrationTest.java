package api.arduinothermohygrometer.configurations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import api.arduinothermohygrometer.properties.OpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiConfigIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private OpenAPI openAPI;

    @Autowired
    private OpenApiProperties openApiProperties;

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

    @Test
    void givenApplicationYaml_whenPropertiesLoaded_thenValuesMatch() {
        assertThat(openApiProperties.getTitle()).isEqualTo("Arduino Thermo Hygrometer API.");
        assertThat(openApiProperties.getDescription()).isEqualTo("A Java Web API for an Arduino Thermo Hygrometer IoT device.");
        assertThat(buildProperties.getVersion()).isEqualTo("1.0.0-SNAPSHOT");
        assertThat(openApiProperties.getContact().getName()).isEqualTo("Burhan Mohammad Sarfraz");
        assertThat(openApiProperties.getContact().getEmail()).isEqualTo("burhan.mohammad.sarfraz@outlook.com");
        assertThat(openApiProperties.getLicense().getName()).isEqualTo("MIT license");
        assertThat(openApiProperties.getLicense().getUrl()).isEqualTo("https://mit-license.org/");
    }

    @Test
    void givenSpringContext_whenOpenApiBeanCreated_thenInfoMatchesProperties() {
        Info info = openAPI.getInfo();

        assertThat(info.getTitle()).isEqualTo(openApiProperties.getTitle());
        assertThat(info.getDescription()).isEqualTo(openApiProperties.getDescription());
        assertThat(info.getVersion()).isEqualTo(buildProperties.getVersion());
        assertThat(info.getContact().getName()).isEqualTo(openApiProperties.getContact().getName());
        assertThat(info.getContact().getEmail()).isEqualTo(openApiProperties.getContact().getEmail());
        assertThat(info.getLicense().getName()).isEqualTo(openApiProperties.getLicense().getName());
        assertThat(info.getLicense().getUrl()).isEqualTo(openApiProperties.getLicense().getUrl());
    }
}
