package api.arduinothermohygrometer;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ApiApplication extends SpringBootServletInitializer {
    public static void main(final String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Oslo"));
        SpringApplication.run(ApiApplication.class, args);
    }
}
