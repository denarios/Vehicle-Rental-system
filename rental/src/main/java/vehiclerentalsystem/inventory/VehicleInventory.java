package vehiclerentalsystem.inventory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import vehiclerentalsystem.model.Vehicle;

public interface VehicleInventory {
    void addVehicle(Vehicle vehicle);
    List<Vehicle> getAllVehicles();
    void removeVehicle(UUID ID);
    default List<Vehicle> getAvailableVehicles() {
        return getAllVehicles()
                .stream()
                .filter(Vehicle::isAvailable)
                .collect(Collectors.toList());
    }

    default List<Vehicle> getUnavailableVehicles() {
        return getAllVehicles()
                .stream()
                .filter(v -> !v.isAvailable())
                .collect(Collectors.toList());
    }
}
