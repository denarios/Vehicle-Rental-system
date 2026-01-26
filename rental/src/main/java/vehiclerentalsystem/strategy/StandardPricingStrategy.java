package vehiclerentalsystem.strategy;

import vehiclerentalsystem.model.Vehicle;

/**
 * Standard pricing strategy with no discounts or surcharges.
 * 
 * Calculation: dailyRate × days
 * 
 * This is the default pricing strategy used when no special
 * conditions apply (not peak season, not long-term rental).
 */
public class StandardPricingStrategy implements PricingStrategy {

    @Override
    public double calculatePrice(Vehicle vehicle, int days) {
        return vehicle.getDailyRate() * days;
    }

    @Override
    public String getStrategyName() {
        return "STANDARD";
    }

    @Override
    public String getDescription() {
        return "Standard pricing - base rate × days";
    }
}
