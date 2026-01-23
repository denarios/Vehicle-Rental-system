package vehiclerentalsystem.inventory;

import vehiclerentalsystem.enums.VehicleType;

public class InventoryFactory {
    

    public static VehicleInventory getInventory(VehicleType type) {

        return switch (type) {
            case BIKE -> new BikeInventory();
            case CAR -> new CarInventory();
        };
    }
}
