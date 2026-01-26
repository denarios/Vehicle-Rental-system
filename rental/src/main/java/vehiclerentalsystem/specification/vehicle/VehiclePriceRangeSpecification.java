package vehiclerentalsystem.specification.vehicle;

import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.specification.Specification;

/**
 * Specification to filter vehicles by daily rate price range.
 * 
 * Usage:
 * // Vehicles priced between $30 and $80 per day
 * Specification<Vehicle> priceSpec = new VehiclePriceRangeSpecification(30.0,
 * 80.0);
 * boolean inRange = priceSpec.isSatisfiedBy(vehicle);
 */
public class VehiclePriceRangeSpecification implements Specification<Vehicle> {

    private final Double minPrice;
    private final Double maxPrice;

    /**
     * Create a price range specification.
     * 
     * @param minPrice Minimum daily rate (null for no minimum)
     * @param maxPrice Maximum daily rate (null for no maximum)
     */
    public VehiclePriceRangeSpecification(Double minPrice, Double maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public boolean isSatisfiedBy(Vehicle vehicle) {
        if (vehicle == null) {
            return false;
        }

        double dailyRate = vehicle.getDailyRate();

        // Check minimum price constraint
        if (minPrice != null && dailyRate < minPrice) {
            return false;
        }

        // Check maximum price constraint
        if (maxPrice != null && dailyRate > maxPrice) {
            return false;
        }

        return true;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    @Override
    public String toString() {
        String min = minPrice != null ? "$" + minPrice : "any";
        String max = maxPrice != null ? "$" + maxPrice : "any";
        return "Price[" + min + " - " + max + "]";
    }
}
