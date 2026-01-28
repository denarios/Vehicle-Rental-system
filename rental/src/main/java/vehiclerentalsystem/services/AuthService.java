package vehiclerentalsystem.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import vehiclerentalsystem.dto.LoginResponse;
import vehiclerentalsystem.model.User;
import vehiclerentalsystem.model.VehicleRentalSystem;

/**
 * Authentication Service.
 * 
 * Handles login logic and user authentication.
 * 
 * In production, this would:
 * - Hash passwords with BCrypt
 * - Generate JWT tokens
 * - Store sessions in Redis
 * - Implement rate limiting on login attempts
 */
@Service
public class AuthService {

    private final VehicleRentalSystem system;

    public AuthService(VehicleRentalSystem system) {
        this.system = system;
    }

    /**
     * Authenticate a user by email and password.
     * 
     * @param email    User's email
     * @param password User's password
     * @return LoginResponse with user info and token
     * @throws IllegalArgumentException if credentials are invalid
     */
    public LoginResponse login(String email, String password) {
        // Find user by email
        User user = system.getAllUsers().stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Verify password (in production: use BCrypt.checkpw())
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Generate token (in production: use JWT)
        String token = "session_" + UUID.randomUUID().toString();

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getAssignedStoreId(),
                token);
    }

    /**
     * Create default users for testing.
     * Called on application startup.
     */
    public void createDefaultUsers() {
        // Customer
        User customer = new User(
                "Pranjal Goyal",
                "pranjalg984@gmail.com",
                "password123",
                true,
                vehiclerentalsystem.enums.UserRole.CUSTOMER);
        system.addUser(customer);

        // Admin
        User admin = new User(
                "Admin User",
                "admin@rental.com",
                "admin123",
                true,
                vehiclerentalsystem.enums.UserRole.ADMIN);
        system.addUser(admin);

        System.out.println("✅ Created default users:");
        System.out.println("   Customer: pranjalg984@gmail.com / password123");
        System.out.println("   Admin: admin@rental.com / admin123");
    }
}
