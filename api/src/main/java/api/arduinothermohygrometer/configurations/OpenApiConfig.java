package api.arduinothermohygrometer.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import api.arduinothermohygrometer.properties.OpenApiProperties;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {
    private final OpenApiProperties openApiProperties;

    public OpenApiConfig(OpenApiProperties openApiProperties) {
        this.openApiProperties = openApiProperties;
    }

    @Bean
    OpenAPI customOpenAPI() {
        Info info = new Info();
        info.setVersion(openApiProperties.getVersion());
        info.setTitle(openApiProperties.getTitle());
        info.setDescription(openApiProperties.getDescription());

        Contact contact = new Contact();
        contact.setName(openApiProperties.getContact().getName());
        contact.setEmail(openApiProperties.getContact().getEmail());
        info.setContact(contact);

        License license = new License();
        license.setName(openApiProperties.getLicense().getName());
        license.setUrl(openApiProperties.getLicense().getUrl());
        info.setLicense(license);

        OpenAPI openApi = new OpenAPI();
        openApi.setInfo(info);

        return openApi;
    }

}
