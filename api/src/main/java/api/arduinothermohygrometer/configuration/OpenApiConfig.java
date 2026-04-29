package api.arduinothermohygrometer.configuration;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import api.arduinothermohygrometer.properties.OpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
    private final BuildProperties buildProperties;
    private final OpenApiProperties openApiProperties;

    public OpenApiConfig(BuildProperties buildProperties, OpenApiProperties openApiProperties) {
        this.buildProperties = buildProperties;
        this.openApiProperties = openApiProperties;
    }

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI().info(
            new Info()
                .version(buildProperties.getVersion())
                .title(openApiProperties.title())
                .description(openApiProperties.description())
                .contact(
                    new Contact()
                        .name(openApiProperties.contact().name())
                        .email(openApiProperties.contact().email())
                )
                .license(
                    new License()
                        .name(openApiProperties.license().name())
                        .url(openApiProperties.license().url())
                )
        );
    }
}
