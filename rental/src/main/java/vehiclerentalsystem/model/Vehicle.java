package vehiclerentalsystem.model;

import java.util.UUID;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;

/**
 * Abstract base class for all vehicle types in the rental system.
 * Implements the Factory Pattern - concrete vehicle types extend this class.
 * 
 * Each vehicle type (Car, Bike, Truck) has:
 * - Different daily rental rates
 * - Different passenger/cargo capacity
 * - Specific rental cost calculation logic
 */
public abstract class Vehicle {
    protected UUID id;
    protected VehicleType type;
    protected VehicleStatus status;
    protected double dailyRate;

    /**
     * Protected constructor - vehicles should be created via VehicleFactory
     */
    protected Vehicle(VehicleType type, VehicleStatus status, double dailyRate) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.status = status;
        this.dailyRate = dailyRate;
    }

    // =============== Abstract Methods ===============

    /**
     * Calculate rental cost for a given number of days.
     * Each vehicle type may have different calculation logic.
     * 
     * @param days Number of rental days
     * @return The base rental cost before any pricing strategy is applied
     */
    public abstract double calculateRentalCost(int days);

    /**
     * Get the passenger/cargo capacity of the vehicle.
     * 
     * @return Capacity (passengers for Car/Bike, tons for Truck)
     */
    public abstract int getCapacity();

    /**
     * Get a description of the vehicle type.
     * 
     * @return Human-readable description
     */
    public abstract String getDescription();

    // =============== Getters ===============

    public UUID getId() {
        return id;
    }

    public VehicleType getType() {
        return type;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public double getDailyRate() {
        return dailyRate;
    }

    // =============== Setters ===============

    public void setType(VehicleType type) {
        this.type = type;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }

    // =============== Utility Methods ===============

    public boolean isAvailable() {
        return status == VehicleStatus.ACTIVE;
    }

    @Override
    public String toString() {
        return String.format("%s [id=%s, dailyRate=%.2f, status=%s]",
                type, id, dailyRate, status);
    }
}
