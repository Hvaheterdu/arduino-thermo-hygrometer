package api.arduinothermohygrometer.base;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import api.arduinothermohygrometer.filter.ApiKeyFilter;
import api.arduinothermohygrometer.properties.OpenApiProperties;
import api.arduinothermohygrometer.properties.SecurityProperties;

public class WebMvcTestBase {
    @MockitoBean
    private ApiKeyFilter apiKeyFilter;

    @MockitoBean
    private OpenApiProperties openApiProperties;

    @MockitoBean
    private SecurityProperties securityProperties;
}
