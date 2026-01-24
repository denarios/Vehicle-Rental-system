package vehiclerentalsystem.controller;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;

public record AddVehicleRequest(
        VehicleType type,
        VehicleStatus status
) {}
