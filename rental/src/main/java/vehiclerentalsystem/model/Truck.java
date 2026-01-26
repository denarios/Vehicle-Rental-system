package vehiclerentalsystem.model;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;

/**
 * Concrete implementation of Vehicle for trucks.
 * Part of the Factory Pattern implementation.
 * 
 * Truck characteristics:
 * - Default daily rate: $100.00 (premium pricing)
 * - Capacity: 10 tons cargo capacity
 * - Includes mileage-based surcharge consideration
 */
public class Truck extends Vehicle {

    private static final double DEFAULT_DAILY_RATE = 100.0;
    private static final int CARGO_CAPACITY_TONS = 10;
    private static final double HEAVY_DUTY_SURCHARGE = 1.15; // 15% surcharge for long rentals

    public Truck(VehicleStatus status) {
        super(VehicleType.TRUCK, status, DEFAULT_DAILY_RATE);
    }

    public Truck(VehicleStatus status, double dailyRate) {
        super(VehicleType.TRUCK, status, dailyRate);
    }

    @Override
    public double calculateRentalCost(int days) {
        // Trucks have a 15% surcharge for rentals over 7 days (heavy duty usage)
        double baseCost = dailyRate * days;
        if (days > 7) {
            return baseCost * HEAVY_DUTY_SURCHARGE;
        }
        return baseCost;
    }

    @Override
    public int getCapacity() {
        return CARGO_CAPACITY_TONS;
    }

    @Override
    public String getDescription() {
        return String.format("Truck - %d ton capacity, $%.2f/day", CARGO_CAPACITY_TONS, dailyRate);
    }
}
