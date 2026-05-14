package api.arduinothermohygrometer.properties;

import java.util.Map;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.servers.ServerVariable;
import io.swagger.v3.oas.models.servers.ServerVariables;
import jakarta.validation.constraints.NotEmpty;

public record OpenApiSingleServerProperties(@NotEmpty String url,
                                            @NotEmpty String description,
                                            @NotEmpty Map<String, OpenApiServerVariableProperties> variables
) {
    public Server generateOpenApiServer() {
        ServerVariables serverVariables = new ServerVariables();
        variables.forEach((key, openApiServerVariableProperties) ->
            serverVariables.addServerVariable(key, new ServerVariable()
                ._default(openApiServerVariableProperties._default())
                ._enum(openApiServerVariableProperties._enum())));

        return new Server()
            .url(url)
            .description(description)
            .variables(serverVariables);
    }
}
