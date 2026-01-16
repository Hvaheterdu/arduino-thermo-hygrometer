package api.arduinothermohygrometer.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.OutputCaptureExtension;

import api.arduinothermohygrometer.entities.Battery;
import api.arduinothermohygrometer.repositories.BatteryRepository;
import api.arduinothermohygrometer.services.implementations.BatteryServiceImpl;

@DisplayName("Unit tests for BatteryServiceImpl")
@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class BatteryServiceImplTest {
    @Mock
    private BatteryRepository batteryRepository;

    @Captor
    ArgumentCaptor<Battery> batteryArgumentCaptor;

    private BatteryServiceImpl batteryService;

    @BeforeEach
    void setUp() {
        batteryService = new BatteryServiceImpl(batteryRepository);
        batteryArgumentCaptor = ArgumentCaptor.forClass(Battery.class);
    }
}
