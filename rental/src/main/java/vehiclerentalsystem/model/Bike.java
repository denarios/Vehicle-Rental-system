package vehiclerentalsystem.model;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;

/**
 * Concrete implementation of Vehicle for bikes/motorcycles.
 * Part of the Factory Pattern implementation.
 * 
 * Bike characteristics:
 * - Default daily rate: $25.00 (most economical)
 * - Capacity: 2 passengers
 * - Standard rental cost calculation
 */
public class Bike extends Vehicle {

    private static final double DEFAULT_DAILY_RATE = 25.0;
    private static final int PASSENGER_CAPACITY = 2;

    public Bike(VehicleStatus status) {
        super(VehicleType.BIKE, status, DEFAULT_DAILY_RATE);
    }

    public Bike(VehicleStatus status, double dailyRate) {
        super(VehicleType.BIKE, status, dailyRate);
    }

    @Override
    public double calculateRentalCost(int days) {
        // Bikes have standard pricing
        return dailyRate * days;
    }

    @Override
    public int getCapacity() {
        return PASSENGER_CAPACITY;
    }

    @Override
    public String getDescription() {
        return String.format("Bike - %d passengers, $%.2f/day", PASSENGER_CAPACITY, dailyRate);
    }
}
