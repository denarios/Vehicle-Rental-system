package vehiclerentalsystem.strategy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import vehiclerentalsystem.model.Vehicle;

/**
 * Context class for the Strategy Pattern.
 * 
 * Manages pricing strategy selection and execution:
 * - Holds a reference to the current pricing strategy
 * - Provides automatic strategy selection based on business rules
 * - Delegates price calculation to the active strategy
 * 
 * Usage:
 * 
 * <pre>
 * PricingContext context = new PricingContext();
 * context.setStrategy(new WeeklyDiscountPricingStrategy());
 * double price = context.calculatePrice(vehicle, 10);
 * 
 * // Or use automatic strategy selection:
 * PricingStrategy strategy = PricingContext.determineStrategy(fromDate, toDate);
 * </pre>
 */
public class PricingContext {

    private PricingStrategy strategy;

    /**
     * Default constructor - uses StandardPricingStrategy.
     */
    public PricingContext() {
        this.strategy = new StandardPricingStrategy();
    }

    /**
     * Constructor with specific strategy.
     * 
     * @param strategy The pricing strategy to use
     */
    public PricingContext(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Set the pricing strategy.
     * 
     * @param strategy The new pricing strategy
     */
    public void setStrategy(PricingStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Get the current pricing strategy.
     * 
     * @return The active pricing strategy
     */
    public PricingStrategy getStrategy() {
        return strategy;
    }

    /**
     * Calculate price using the current strategy.
     * 
     * @param vehicle The vehicle being rented
     * @param days    Number of rental days
     * @return The calculated price
     */
    public double calculatePrice(Vehicle vehicle, int days) {
        return strategy.calculatePrice(vehicle, days);
    }

    /**
     * Get the name of the current strategy.
     * 
     * @return Strategy name
     */
    public String getStrategyName() {
        return strategy.getStrategyName();
    }

    /**
     * Automatically determine the best pricing strategy based on dates.
     * 
     * Strategy selection rules (in priority order):
     * 1. Peak Season: If rental starts in December or January
     * 2. Weekly Discount: If rental is 7+ days
     * 3. Standard: Default for all other cases
     * 
     * @param fromDate Start date of the rental
     * @param toDate   End date of the rental
     * @return The appropriate pricing strategy
     */
    public static PricingStrategy determineStrategy(LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null || toDate == null) {
            return new StandardPricingStrategy();
        }

        int month = fromDate.getMonthValue();
        long days = ChronoUnit.DAYS.between(fromDate, toDate) + 1; // inclusive

        // Peak season months: December (12) and January (1)
        if (month == 12 || month == 1) {
            return new PeakSeasonPricingStrategy();
        }

        // Weekly discount for 7+ days
        if (days >= 7) {
            return new WeeklyDiscountPricingStrategy();
        }

        // Default to standard pricing
        return new StandardPricingStrategy();
    }

    /**
     * Calculate price with automatic strategy selection.
     * 
     * @param vehicle  The vehicle being rented
     * @param fromDate Start date of the rental
     * @param toDate   End date of the rental
     * @return The calculated price using the best strategy
     */
    public static double calculateWithBestStrategy(Vehicle vehicle, LocalDate fromDate, LocalDate toDate) {
        long days = ChronoUnit.DAYS.between(fromDate, toDate) + 1;
        PricingStrategy strategy = determineStrategy(fromDate, toDate);
        return strategy.calculatePrice(vehicle, (int) days);
    }
}
