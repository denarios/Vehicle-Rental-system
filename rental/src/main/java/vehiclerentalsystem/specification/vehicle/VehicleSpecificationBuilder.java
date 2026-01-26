package vehiclerentalsystem.specification.vehicle;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.specification.Specification;

/**
 * Builder class to easily construct vehicle search specifications.
 * Provides a fluent API for combining multiple filter criteria.
 * 
 * Usage:
 * 
 * <pre>
 * Specification<Vehicle> spec = VehicleSpecificationBuilder.builder()
 *         .withType(VehicleType.CAR)
 *         .withStatus(VehicleStatus.ACTIVE)
 *         .withPriceRange(30.0, 80.0)
 *         .build();
 * </pre>
 */
public class VehicleSpecificationBuilder {

    private Specification<Vehicle> specification;

    private VehicleSpecificationBuilder() {
        // Start with a specification that matches everything
        this.specification = vehicle -> true;
    }

    /**
     * Create a new builder instance.
     */
    public static VehicleSpecificationBuilder builder() {
        return new VehicleSpecificationBuilder();
    }

    /**
     * Filter by vehicle type.
     * 
     * @param type The vehicle type (CAR, BIKE, TRUCK)
     * @return this builder for chaining
     */
    public VehicleSpecificationBuilder withType(VehicleType type) {
        if (type != null) {
            specification = specification.and(new VehicleTypeSpecification(type));
        }
        return this;
    }

    /**
     * Filter by vehicle status.
     * 
     * @param status The vehicle status (ACTIVE, INACTIVE)
     * @return this builder for chaining
     */
    public VehicleSpecificationBuilder withStatus(VehicleStatus status) {
        if (status != null) {
            specification = specification.and(new VehicleStatusSpecification(status));
        }
        return this;
    }

    /**
     * Filter by price range.
     * 
     * @param minPrice Minimum daily rate (null for no minimum)
     * @param maxPrice Maximum daily rate (null for no maximum)
     * @return this builder for chaining
     */
    public VehicleSpecificationBuilder withPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice != null || maxPrice != null) {
            specification = specification.and(new VehiclePriceRangeSpecification(minPrice, maxPrice));
        }
        return this;
    }

    /**
     * Filter by minimum price only.
     * 
     * @param minPrice Minimum daily rate
     * @return this builder for chaining
     */
    public VehicleSpecificationBuilder withMinPrice(Double minPrice) {
        return withPriceRange(minPrice, null);
    }

    /**
     * Filter by maximum price only.
     * 
     * @param maxPrice Maximum daily rate
     * @return this builder for chaining
     */
    public VehicleSpecificationBuilder withMaxPrice(Double maxPrice) {
        return withPriceRange(null, maxPrice);
    }

    /**
     * Build the final specification.
     * 
     * @return The combined specification
     */
    public Specification<Vehicle> build() {
        return specification;
    }
}
