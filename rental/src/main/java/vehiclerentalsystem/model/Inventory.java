package vehiclerentalsystem.model;

import java.util.HashMap;
import java.util.Map;

import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.inventory.*;

public class Inventory {

    private Map<VehicleType, VehicleInventory> inventoryMap = new HashMap<>();

    public void addVehicle(Vehicle vehicle) {
        VehicleType type = vehicle.getType();

        // Create inventory only if not present
        inventoryMap.computeIfAbsent(type, t -> {
            if (t == VehicleType.BIKE) return new BikeInventory();
            if (t == VehicleType.CAR) return new CarInventory();
            throw new IllegalArgumentException("Unknown vehicle type");
        }).addVehicle(vehicle);
    }

    public VehicleInventory getInventory(VehicleType type) {
        return inventoryMap.get(type);
    }
    public Map<VehicleType, VehicleInventory> getAllVehicle(){
        return inventoryMap;
    } 
}
