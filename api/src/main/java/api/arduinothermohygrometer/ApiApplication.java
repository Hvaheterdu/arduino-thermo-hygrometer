package api.arduinothermohygrometer;

import java.util.TimeZone;

import org.jspecify.annotations.NullMarked;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ApiApplication extends SpringBootServletInitializer {
    @Override
    @NullMarked
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApiApplication.class);
    }

    static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Oslo"));
        SpringApplication.run(ApiApplication.class, args);
    }
}
