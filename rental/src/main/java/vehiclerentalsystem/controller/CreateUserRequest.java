package vehiclerentalsystem.controller;

public record CreateUserRequest(
        String name,
        boolean drivingLicence
) {}
