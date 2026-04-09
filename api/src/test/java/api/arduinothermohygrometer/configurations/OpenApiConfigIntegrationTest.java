package api.arduinothermohygrometer.configurations;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;

import api.arduinothermohygrometer.properties.OpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OpenApiConfigIntegrationTest {
    @Autowired
    private BuildProperties buildProperties;

    @Autowired
    private OpenAPI openAPI;

    @Autowired
    private OpenApiProperties openApiProperties;

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
