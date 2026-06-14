package api.arduinothermohygrometer.configuration;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import api.arduinothermohygrometer.properties.OpenApiProperties;
import api.arduinothermohygrometer.properties.OpenApiServerProperties;
import api.arduinothermohygrometer.properties.OpenApiSingleServerProperties;
import api.arduinothermohygrometer.properties.SecurityProperties;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {
    private final BuildProperties buildProperties;
    private final OpenApiProperties openApiProperties;
    private final OpenApiServerProperties openApiServerProperties;
    private final SecurityProperties securityProperties;

    public OpenApiConfig(final BuildProperties buildProperties, final OpenApiProperties openApiProperties,
                         final OpenApiServerProperties openApiServerProperties, final SecurityProperties securityProperties) {
        this.buildProperties = buildProperties;
        this.openApiProperties = openApiProperties;
        this.openApiServerProperties = openApiServerProperties;
        this.securityProperties = securityProperties;
    }

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
            .addSecurityItem(new SecurityRequirement()
                                 .addList(securityProperties.apiSchemeName()))
            .components(new Components()
                            .addSecuritySchemes(securityProperties.apiSchemeName(),
                                                new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER)
                                                                    .name(securityProperties.apiHeaderName())))
            .info(new Info()
                      .version(buildProperties.getVersion())
                      .title(openApiProperties.title())
                      .description(openApiProperties.description())
                      .contact(new Contact()
                                   .name(openApiProperties.contact().name())
                                   .email(openApiProperties.contact().email()))
                      .license(new License()
                                   .name(openApiProperties.license().name())
                                   .url(openApiProperties.license().url())))
            .servers(openApiServerProperties.servers().stream().map(OpenApiSingleServerProperties::generateOpenApiServer).toList());
    }
}
