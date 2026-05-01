package api.arduinothermohygrometer.repository;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import api.arduinothermohygrometer.manager.TestcontainerManager;

@DisplayName("BatteryRepositoryImpl integration tests.")
@SpringBootTest
@Sql("classpath:sql/insert_batteries.sql")
@Transactional
class BatteryRepositoryImplIT extends TestcontainerManager {
    @Autowired
    private BatteryRepository batteryRepository;

    @MockitoBean
    protected BuildProperties buildProperties;
}
