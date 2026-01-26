package vehiclerentalsystem.factory;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.Bike;
import vehiclerentalsystem.model.Car;
import vehiclerentalsystem.model.Truck;
import vehiclerentalsystem.model.Vehicle;

/**
 * Factory class for creating Vehicle instances.
 * 
 * Implements the Factory Design Pattern:
 * - Encapsulates vehicle creation logic
 * - Provides a single point of vehicle instantiation
 * - Makes it easy to add new vehicle types without modifying client code
 * 
 * Usage:
 * 
 * <pre>
 * Vehicle car = VehicleFactory.createVehicle(VehicleType.CAR, VehicleStatus.ACTIVE);
 * Vehicle bike = VehicleFactory.createVehicle(VehicleType.BIKE, VehicleStatus.ACTIVE, 30.0);
 * </pre>
 */
public class VehicleFactory {

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with static methods only.
     */
    private VehicleFactory() {
        throw new UnsupportedOperationException("Factory class cannot be instantiated");
    }

    /**
     * Create a vehicle with default daily rate based on type.
     * 
     * @param type   The type of vehicle to create (CAR, BIKE, TRUCK)
     * @param status The initial status of the vehicle (ACTIVE, INACTIVE, DAMAGED)
     * @return A new Vehicle instance of the appropriate type
     * @throws IllegalArgumentException if type is null or unknown
     */
    public static Vehicle createVehicle(VehicleType type, VehicleStatus status) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Vehicle status cannot be null");
        }

        return switch (type) {
            case CAR -> new Car(status);
            case BIKE -> new Bike(status);
            case TRUCK -> new Truck(status);
        };
    }

    /**
     * Create a vehicle with a custom daily rate.
     * 
     * @param type      The type of vehicle to create
     * @param status    The initial status of the vehicle
     * @param dailyRate Custom daily rental rate
     * @return A new Vehicle instance with the specified rate
     * @throws IllegalArgumentException if type is null, unknown, or dailyRate is
     *                                  negative
     */
    public static Vehicle createVehicle(VehicleType type, VehicleStatus status, double dailyRate) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Vehicle status cannot be null");
        }
        if (dailyRate < 0) {
            throw new IllegalArgumentException("Daily rate cannot be negative");
        }

        return switch (type) {
            case CAR -> new Car(status, dailyRate);
            case BIKE -> new Bike(status, dailyRate);
            case TRUCK -> new Truck(status, dailyRate);
        };
    }

    /**
     * Get the default daily rate for a vehicle type.
     * Useful for displaying pricing information.
     * 
     * @param type The vehicle type
     * @return The default daily rate for that type
     */
    public static double getDefaultDailyRate(VehicleType type) {
        return switch (type) {
            case CAR -> 50.0;
            case BIKE -> 25.0;
            case TRUCK -> 100.0;
        };
    }
}
