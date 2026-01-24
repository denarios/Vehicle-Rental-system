package vehiclerentalsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vehiclerentalsystem.model.VehicleRentalSystem;

@Configuration
public class AppConfig {

    @Bean
    public VehicleRentalSystem vehicleRentalSystem() {
        return new VehicleRentalSystem();
    }
}
