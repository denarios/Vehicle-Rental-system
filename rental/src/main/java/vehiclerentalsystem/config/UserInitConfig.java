package vehiclerentalsystem.config;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import vehiclerentalsystem.services.AuthService;

/**
 * Initializes default users on application startup.
 */
@Configuration
public class UserInitConfig {

    private final AuthService authService;

    public UserInitConfig(AuthService authService) {
        this.authService = authService;
    }

    @PostConstruct
    public void initDefaultUsers() {
        authService.createDefaultUsers();
    }
}
