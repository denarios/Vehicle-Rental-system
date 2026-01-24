package vehiclerentalsystem.controller;

import vehiclerentalsystem.enums.VehicleType;

public record CreateVehicleRequest(
        VehicleType type,
        String model,
        double pricePerDay
) {}
