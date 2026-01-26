package vehiclerentalsystem.strategy;

import vehiclerentalsystem.model.Vehicle;

/**
 * Strategy interface for calculating rental prices.
 * 
 * Implements the Strategy Design Pattern:
 * - Defines a family of pricing algorithms
 * - Each pricing strategy is encapsulated in its own class
 * - Strategies are interchangeable at runtime
 * 
 * Common strategies:
 * - StandardPricingStrategy: Base rate calculation
 * - PeakSeasonPricingStrategy: 20% premium during holidays
 * - WeeklyDiscountPricingStrategy: 10% off for 7+ day rentals
 */
public interface PricingStrategy {

    /**
     * Calculate the total rental price for a vehicle.
     * 
     * @param vehicle The vehicle being rented
     * @param days    Number of rental days
     * @return The calculated total price
     */
    double calculatePrice(Vehicle vehicle, int days);

    /**
     * Get the name of this pricing strategy.
     * Used for logging and display purposes.
     * 
     * @return Strategy name (e.g., "STANDARD", "PEAK_SEASON", "WEEKLY_DISCOUNT")
     */
    String getStrategyName();

    /**
     * Get a description of the pricing strategy.
     * 
     * @return Human-readable description of the pricing logic
     */
    String getDescription();
}
