package vehiclerentalsystem.specification.vehicle;

import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.specification.Specification;

/**
 * Specification to filter vehicles by type (CAR, BIKE, TRUCK).
 * 
 * Usage:
 * Specification<Vehicle> carSpec = new
 * VehicleTypeSpecification(VehicleType.CAR);
 * boolean isCar = carSpec.isSatisfiedBy(vehicle);
 */
public class VehicleTypeSpecification implements Specification<Vehicle> {

    private final VehicleType type;

    public VehicleTypeSpecification(VehicleType type) {
        this.type = type;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        if (vehicle == null || type == null) {
            return false;
        }
        return vehicle.getType() == type;
    }

    public VehicleType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "VehicleType=" + type;
    }
}
