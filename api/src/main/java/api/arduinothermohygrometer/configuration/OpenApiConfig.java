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
        Info info = new Info();
        info.setVersion(buildProperties.getVersion());
        info.setTitle(openApiProperties.getTitle());
        info.setDescription(openApiProperties.getDescription());

        Contact contact = new Contact();
        contact.setName(openApiProperties.getContact().name());
        contact.setEmail(openApiProperties.getContact().email());
        info.setContact(contact);

        License license = new License();
        license.setName(openApiProperties.getLicense().name());
        license.setUrl(openApiProperties.getLicense().url());
        info.setLicense(license);

        OpenAPI openApi = new OpenAPI();
        openApi.setInfo(info);

        return openApi;
    }
}
