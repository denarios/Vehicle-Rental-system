package vehiclerentalsystem.strategy;

import vehiclerentalsystem.model.Vehicle;

/**
 * Peak season pricing strategy with 20% surcharge.
 * 
 * Calculation: dailyRate × days × 1.20
 * 
 * Applied during high-demand periods:
 * - December (Christmas/New Year holidays)
 * - January (New Year)
 * - Summer vacation months (optional, configurable)
 */
public class PeakSeasonPricingStrategy implements PricingStrategy {

    private static final double PEAK_SEASON_MULTIPLIER = 1.20; // 20% surcharge

    @Override
    public double calculatePrice(Vehicle vehicle, int days) {
        double basePrice = vehicle.getDailyRate() * days;
        return basePrice * PEAK_SEASON_MULTIPLIER;
    }

    @Override
    public String getStrategyName() {
        return "PEAK_SEASON";
    }

    @Override
    public String getDescription() {
        return "Peak season pricing - 20% surcharge applied";
    }

    /**
     * Get the peak season multiplier.
     * 
     * @return The multiplier (1.20 = 20% increase)
     */
    public double getMultiplier() {
        return PEAK_SEASON_MULTIPLIER;
    }
}
