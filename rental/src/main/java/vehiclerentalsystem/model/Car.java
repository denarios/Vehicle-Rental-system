package vehiclerentalsystem.model;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;

/**
 * Concrete implementation of Vehicle for cars.
 * Part of the Factory Pattern implementation.
 * 
 * Car characteristics:
 * - Default daily rate: $50.00
 * - Capacity: 5 passengers
 * - Standard rental cost calculation
 */
public class Car extends Vehicle {

    private static final double DEFAULT_DAILY_RATE = 50.0;
    private static final int PASSENGER_CAPACITY = 5;

    public Car(VehicleStatus status) {
        super(VehicleType.CAR, status, DEFAULT_DAILY_RATE);
    }

    public Car(VehicleStatus status, double dailyRate) {
        super(VehicleType.CAR, status, dailyRate);
    }

    @Override
    public double calculateRentalCost(int days) {
        // Cars have standard pricing
        return dailyRate * days;
    }

    @Override
    public int getCapacity() {
        return PASSENGER_CAPACITY;
    }

    @Override
    public String getDescription() {
        return String.format("Car - %d passengers, $%.2f/day", PASSENGER_CAPACITY, dailyRate);
    }
}
