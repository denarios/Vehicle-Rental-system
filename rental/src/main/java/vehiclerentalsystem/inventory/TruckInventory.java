package vehiclerentalsystem.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import vehiclerentalsystem.model.Vehicle;

/**
 * Inventory for Truck vehicles.
 * Added to support the Factory Pattern with TRUCK vehicle type.
 */
public class TruckInventory implements VehicleInventory {

    private List<Vehicle> vehicleList = new ArrayList<>();

    @Override
    public void addVehicle(Vehicle vehicle) {
        vehicleList.add(vehicle);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleList;
    }

    @Override
    public void removeVehicle(UUID vehicleId) {
        vehicleList.removeIf(vehicle -> vehicle.getId().equals(vehicleId));
    }
}
