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

@AutoConfigureMockMvc
@DisplayName("Tests for OpenAPI configuration.")
@SpringBootTest
class OpenApiConfigIT {
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
    @DisplayName("Given application.yaml when properties are loaded then property values match.")
    void givenApplicationYaml_whenPropertiesLoaded_thenPropertyValuesMatch() {
        assertThat(openApiProperties.title()).isEqualTo("Arduino Thermo Hygrometer API.");
        assertThat(openApiProperties.description()).isEqualTo("A Java Web API for an Arduino Thermo Hygrometer IoT device.");
        assertThat(buildProperties.getVersion()).isEqualTo("1.0.0-SNAPSHOT");
        assertThat(openApiProperties.contact().name()).isEqualTo("Burhan Mohammad Sarfraz");
        assertThat(openApiProperties.contact().email()).isEqualTo("burhan.mohammad.sarfraz@outlook.com");
        assertThat(openApiProperties.license().name()).isEqualTo("MIT license");
        assertThat(openApiProperties.license().url()).isEqualTo("https://mit-license.org/");
    }

    @Test
    @DisplayName("Given Spring Context when creating OpenApi bean then info properties match.")
    void givenSpringContext_whenOpenApiBeanCreated_thenInfoMatchesProperties() {
        Info info = openAPI.getInfo();

        assertThat(info.getTitle()).isEqualTo(openApiProperties.title());
        assertThat(info.getDescription()).isEqualTo(openApiProperties.description());
        assertThat(info.getVersion()).isEqualTo(buildProperties.getVersion());
        assertThat(info.getContact().getName()).isEqualTo(openApiProperties.contact().name());
        assertThat(info.getContact().getEmail()).isEqualTo(openApiProperties.contact().email());
        assertThat(info.getLicense().getName()).isEqualTo(openApiProperties.license().name());
        assertThat(info.getLicense().getUrl()).isEqualTo(openApiProperties.license().url());
    }
}
