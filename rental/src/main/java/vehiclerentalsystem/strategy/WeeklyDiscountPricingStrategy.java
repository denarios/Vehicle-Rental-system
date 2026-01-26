package vehiclerentalsystem.strategy;

import vehiclerentalsystem.model.Vehicle;

/**
 * Weekly discount pricing strategy with 10% discount for 7+ day rentals.
 * 
 * Calculation:
 * - If days < 7: dailyRate × days (no discount)
 * - If days >= 7: dailyRate × days × 0.90 (10% off)
 * 
 * Encourages longer rentals which are more profitable due to
 * reduced vehicle turnover and cleaning costs.
 */
public class WeeklyDiscountPricingStrategy implements PricingStrategy {

    private static final double WEEKLY_DISCOUNT_MULTIPLIER = 0.90; // 10% discount
    private static final int MINIMUM_DAYS_FOR_DISCOUNT = 7;

    @Override
    public double calculatePrice(Vehicle vehicle, int days) {
        double basePrice = vehicle.getDailyRate() * days;

        if (days >= MINIMUM_DAYS_FOR_DISCOUNT) {
            return basePrice * WEEKLY_DISCOUNT_MULTIPLIER;
        }

        return basePrice;
    }

    @Override
    public String getStrategyName() {
        return "WEEKLY_DISCOUNT";
    }

    @Override
    public String getDescription() {
        return "Weekly discount - 10% off for rentals of 7+ days";
    }

    /**
     * Get the minimum days required for the discount.
     * 
     * @return Minimum days (7)
     */
    public int getMinimumDaysForDiscount() {
        return MINIMUM_DAYS_FOR_DISCOUNT;
    }

    /**
     * Get the discount multiplier.
     * 
     * @return The multiplier (0.90 = 10% discount)
     */
    public double getDiscountMultiplier() {
        return WEEKLY_DISCOUNT_MULTIPLIER;
    }
}
