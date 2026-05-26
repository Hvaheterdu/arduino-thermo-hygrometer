package api.arduinothermohygrometer.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;

import api.arduinothermohygrometer.properties.OpenApiProperties;
import api.arduinothermohygrometer.properties.OpenApiServerProperties;
import api.arduinothermohygrometer.properties.OpenApiSingleServerProperties;

@AutoConfigureMockMvc
@DisplayName("OpenAPI configuration MVC slice integration tests.")
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

    @Autowired
    private OpenApiServerProperties openApiServerProperties;

    @Test
    void givenSwaggerUiEndpoint_whenAccessed_thenReturn200OK() {
        mockMvcTester.get()
                .uri("/swagger-ui/index.html")
                .exchange()
                .assertThat()
                .hasStatusOk();
    }

    @Test
    void givenOpenApiDocsEndpoint_whenAccessed_thenReturn200OK() {
        mockMvcTester.get()
                .uri("/v3/api-docs")
                .exchange()
                .assertThat()
                .hasStatusOk();
    }

    @Test
    void givenApplicationYaml_whenInfoPropertiesLoaded_thenPropertyValuesMatch() {
        assertThat(openApiProperties.title()).isEqualTo("Arduino Thermo Hygrometer API.");
        assertThat(openApiProperties.description()).isEqualTo("A Java RESTful API for an Arduino Thermo Hygrometer IoT device.");
        assertThat(buildProperties.getVersion()).isEqualTo("1.0.0-SNAPSHOT");
        assertThat(openApiProperties.contact().name()).isEqualTo("Burhan Mohammad Sarfraz");
        assertThat(openApiProperties.contact().email()).isEqualTo("burhan.mohammad.sarfraz@outlook.com");
        assertThat(openApiProperties.license().name()).isEqualTo("MIT license");
        assertThat(openApiProperties.license().url()).isEqualTo("https://mit-license.org/");
    }

    @Test
    void givenSpringContext_whenOpenApiBeanCreated_thenInfoPropertiesMatch() {
        Info info = openAPI.getInfo();

        assertThat(info.getTitle()).isEqualTo(openApiProperties.title());
        assertThat(info.getDescription()).isEqualTo(openApiProperties.description());
        assertThat(info.getVersion()).isEqualTo(buildProperties.getVersion());
        assertThat(info.getContact().getName()).isEqualTo(openApiProperties.contact().name());
        assertThat(info.getContact().getEmail()).isEqualTo(openApiProperties.contact().email());
        assertThat(info.getLicense().getName()).isEqualTo(openApiProperties.license().name());
        assertThat(info.getLicense().getUrl()).isEqualTo(openApiProperties.license().url());
    }

    @Test
    void givenSpringContext_whenOpenApiBeanCreated_thenServerPropertiesMatch() {
        List<Server> servers = openAPI.getServers();
        OpenApiSingleServerProperties openApiSingleServerProperties = openApiServerProperties.servers().getFirst();

        assertThat(servers)
                .hasSize(1)
                .first()
                .satisfies(server -> {
                    assertThat(server.getUrl()).isEqualTo(openApiSingleServerProperties.url());
                    assertThat(server.getDescription()).isEqualTo(openApiSingleServerProperties.description());
                    assertThat(server.getVariables().keySet()).isEqualTo(openApiSingleServerProperties.variables().keySet());
                    ServerVariable hostServerVariable = server.getVariables().get("host");
                    assertThat(hostServerVariable.getDefault()).isEqualTo(openApiSingleServerProperties.variables().get("host")._default());
                    ServerVariable portServerVariable = server.getVariables().get("port");
                    assertThat(portServerVariable.getDefault()).isEqualTo(openApiSingleServerProperties.variables().get("port")._default());
                });
    }
}
