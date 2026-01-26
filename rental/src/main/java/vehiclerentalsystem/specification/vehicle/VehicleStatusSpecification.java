package vehiclerentalsystem.specification.vehicle;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.specification.Specification;

/**
 * Specification to filter vehicles by status (ACTIVE, INACTIVE, DAMAGED).
 * 
 * Usage:
 * Specification<Vehicle> activeSpec = new
 * VehicleStatusSpecification(VehicleStatus.ACTIVE);
 * boolean isActive = activeSpec.isSatisfiedBy(vehicle);
 */
public class VehicleStatusSpecification implements Specification<Vehicle> {

    private final VehicleStatus status;

    public VehicleStatusSpecification(VehicleStatus status) {
        this.status = status;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        if (vehicle == null || status == null) {
            return false;
        }
        return vehicle.getStatus() == status;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "VehicleStatus=" + status;
    }
}
