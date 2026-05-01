package api.arduinothermohygrometer.repository;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import api.arduinothermohygrometer.manager.TestcontainerManager;

@DisplayName("TemperatureRepositoryImpl integration tests.")
@SpringBootTest
@Sql("classpath:sql/insert_temperatures.sql")
@Transactional
class TemperatureRepositoryImplIT extends TestcontainerManager {
    @Autowired
    private TemperatureRepository temperatureRepository;

    @MockitoBean
    protected BuildProperties buildProperties;
}
