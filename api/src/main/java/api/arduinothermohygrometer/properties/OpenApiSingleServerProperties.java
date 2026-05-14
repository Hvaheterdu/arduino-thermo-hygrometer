package api.arduinothermohygrometer.properties;

import io.swagger.v3.oas.models.servers.Server;
import jakarta.validation.constraints.NotEmpty;

public record OpenApiSingleServerProperties(@NotEmpty String url,
                                            @NotEmpty String description
) {
    public Server generateOpenApiServer() {
        return new Server()
            .url(url)
            .description(description);
    }
}
