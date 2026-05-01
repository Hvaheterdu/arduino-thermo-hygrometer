package api.arduinothermohygrometer.base;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

import api.arduinothermohygrometer.filter.ApiKeyFilter;
import api.arduinothermohygrometer.properties.CorsProperties;
import api.arduinothermohygrometer.properties.OpenApiProperties;
import api.arduinothermohygrometer.properties.SecurityProperties;

public class WebMvcTestBase {
    @MockitoBean
    protected ApiKeyFilter apiKeyFilter;

    @MockitoBean
    protected CorsProperties corsProperties;

    @MockitoBean
    protected OpenApiProperties openApiProperties;

    @MockitoBean
    protected SecurityProperties securityProperties;
}
