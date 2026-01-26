package vehiclerentalsystem.dto;

import java.util.UUID;

import vehiclerentalsystem.enums.UserRole;

/**
 * Login response DTO.
 * Contains user info and role for frontend routing.
 */
public record LoginResponse(
        UUID userId,
        String name,
        String email,
        UserRole role,
        UUID assignedStoreId, // For store managers
        String token // In production: JWT token
) {
}
