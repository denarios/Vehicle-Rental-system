package vehiclerentalsystem.services;

import org.springframework.stereotype.Service;
import vehiclerentalsystem.model.User;
import vehiclerentalsystem.model.VehicleRentalSystem;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final VehicleRentalSystem system;

    public UserService(VehicleRentalSystem system) {
        this.system = system;
    }

    public User createUser(String name, boolean drivingLicence) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("User name cannot be empty");
        }
        User user = new User(name, drivingLicence);
        system.addUser(user);
        return user;
    }

    public List<User> getAllUsers() {
        return system.getAllUsers();
    }

    public User getUserById(UUID userId) {
        return system.getUserById(userId);
    }
}
