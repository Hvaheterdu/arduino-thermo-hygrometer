package api.arduinothermohygrometer.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import api.arduinothermohygrometer.properties.OpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Tests for OpenAPI configuration.")
@AutoConfigureMockMvc
class OpenApiConfigIntegrationTest {
    @Autowired
    private MockMvcTester mockMvcTester;

    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private OpenAPI openAPI;

    @Autowired
    private OpenApiProperties openApiProperties;

    @Test
    @DisplayName("Given Swagger UI endpoint when accessed then return 200 OK")
    void givenSwaggerUiEndpoint_whenAccessed_thenReturn200OK() {
        mockMvcTester.get()
                     .uri("/swagger-ui/index.html")
                     .exchange()
                     .assertThat()
                     .hasStatusOk();
    }

    @Test
    @DisplayName("Given OpenAPI docs endpoint when accessed then return 200 OK")
    void givenOpenApiDocsEndpoint_whenAccessed_thenReturn200OK() {
        mockMvcTester.get()
                     .uri("/v3/api-docs")
                     .exchange()
                     .assertThat()
                     .hasStatusOk();
    }

    @Test
    void givenApplicationYaml_whenPropertiesLoaded_thenValuesMatch() {
        assertThat(openApiProperties.getTitle()).isEqualTo("Arduino Thermo Hygrometer API.");
        assertThat(openApiProperties.getDescription()).isEqualTo("A Java Web API for an Arduino Thermo Hygrometer IoT device.");
        assertThat(buildProperties.getVersion()).isEqualTo("1.0.0-SNAPSHOT");
        assertThat(openApiProperties.getContact().name()).isEqualTo("Burhan Mohammad Sarfraz");
        assertThat(openApiProperties.getContact().email()).isEqualTo("burhan.mohammad.sarfraz@outlook.com");
        assertThat(openApiProperties.getLicense().name()).isEqualTo("MIT license");
        assertThat(openApiProperties.getLicense().url()).isEqualTo("https://mit-license.org/");
    }

    @Test
    void givenSpringContext_whenOpenApiBeanCreated_thenInfoMatchesProperties() {
        Info info = openAPI.getInfo();

        assertThat(info.getTitle()).isEqualTo(openApiProperties.getTitle());
        assertThat(info.getDescription()).isEqualTo(openApiProperties.getDescription());
        assertThat(info.getVersion()).isEqualTo(buildProperties.getVersion());
        assertThat(info.getContact().getName()).isEqualTo(openApiProperties.getContact().name());
        assertThat(info.getContact().getEmail()).isEqualTo(openApiProperties.getContact().email());
        assertThat(info.getLicense().getName()).isEqualTo(openApiProperties.getLicense().name());
        assertThat(info.getLicense().getUrl()).isEqualTo(openApiProperties.getLicense().url());
    }
}
