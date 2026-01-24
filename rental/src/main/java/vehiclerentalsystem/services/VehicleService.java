package vehiclerentalsystem.services;

import org.springframework.stereotype.Service;
import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.Store;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.model.VehicleRentalSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class VehicleService {

    private final VehicleRentalSystem system;

    public VehicleService(VehicleRentalSystem system) {
        this.system = system;
    }

    public Vehicle addVehicle(UUID storeId, VehicleType type, VehicleStatus status) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Vehicle status cannot be null");
        }

        Store store = system.getStoreById(storeId);
        Vehicle vehicle = new Vehicle(type, status);
        store.addVehicle(vehicle);
        return vehicle;
    }

    public Map<VehicleType, List<Vehicle>> getAvailableVehicles(UUID storeId) {
        Store store = system.getStoreById(storeId);
        return store.getAvailableVehicles();
    }

    public Map<VehicleType, List<Vehicle>> getAllVehicles(UUID storeId) {
        Store store = system.getStoreById(storeId);
        Map<VehicleType, List<Vehicle>> available = store.getAvailableVehicles();
        Map<VehicleType, List<Vehicle>> unavailable = store.getUnavailableVehicles();

        // Merge both maps
        unavailable.forEach((type, vehicles) -> available.merge(type, vehicles, (v1, v2) -> {
            List<Vehicle> merged = new ArrayList<>(v1);
            merged.addAll(v2);
            return merged;
        }));

        return available;
    }

    public Vehicle getVehicleById(UUID storeId, UUID vehicleId) {
        Store store = system.getStoreById(storeId);
        return store.getVehicleById(vehicleId);
    }
}
