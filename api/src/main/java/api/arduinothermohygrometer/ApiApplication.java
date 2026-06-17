package api.arduinothermohygrometer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.util.TimeZone;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ApiApplication {
    public static void main(final String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Oslo"));
        SpringApplication.run(ApiApplication.class, args);
    }
}
