package api.arduinothermohygrometer.configurations;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http2.Http2Protocol;
import org.springframework.boot.tomcat.servlet.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.boot.tomcat.TomcatWebServerFactory.DEFAULT_PROTOCOL;

@Configuration
public class HttpsRedirectConfig {
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> getWebServerFactoryCustomizer() {
        return webServerFactor -> {
            Connector httpConnector = new Connector(DEFAULT_PROTOCOL);

            httpConnector.setPort(8080);
            webServerFactor.addConnectorCustomizers(
                    connector -> connector.addUpgradeProtocol(new Http2Protocol()));
            webServerFactor.addAdditionalConnectors(httpConnector);
        };
    }
}
