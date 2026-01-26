package vehiclerentalsystem.dto;

/**
 * Login request DTO.
 */
public record LoginRequest(
        String email,
        String password) {
}
