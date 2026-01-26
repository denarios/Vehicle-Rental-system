package vehiclerentalsystem.services;

import org.springframework.stereotype.Service;
import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.factory.VehicleFactory;
import vehiclerentalsystem.model.Store;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.model.VehicleRentalSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Service layer for vehicle management.
 * 
 * Uses the Factory Pattern via VehicleFactory to create vehicles.
 * This decouples the service from specific vehicle implementations.
 */
@Service
public class VehicleService {

    private final VehicleRentalSystem system;

    public VehicleService(VehicleRentalSystem system) {
        this.system = system;
    }

    /**
     * Add a new vehicle to a store using the Factory Pattern.
     * 
     * @param storeId Store to add vehicle to
     * @param type    Vehicle type (CAR, BIKE, TRUCK)
     * @param status  Initial vehicle status
     * @return The created vehicle
     */
    public Vehicle addVehicle(UUID storeId, VehicleType type, VehicleStatus status) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Vehicle status cannot be null");
        }

        Store store = system.getStoreById(storeId);

        // Using Factory Pattern instead of direct instantiation
        Vehicle vehicle = VehicleFactory.createVehicle(type, status);

        store.addVehicle(vehicle);
        return vehicle;
    }

    /**
     * Add a new vehicle with a custom daily rate.
     * 
     * @param storeId   Store to add vehicle to
     * @param type      Vehicle type
     * @param status    Initial vehicle status
     * @param dailyRate Custom daily rental rate
     * @return The created vehicle
     */
    public Vehicle addVehicle(UUID storeId, VehicleType type, VehicleStatus status, double dailyRate) {
        if (type == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Vehicle status cannot be null");
        }
        if (dailyRate < 0) {
            throw new IllegalArgumentException("Daily rate cannot be negative");
        }

        Store store = system.getStoreById(storeId);

        // Using Factory Pattern with custom daily rate
        Vehicle vehicle = VehicleFactory.createVehicle(type, status, dailyRate);

        store.addVehicle(vehicle);
        return vehicle;
    }

    /**
     * Get available vehicles at a store.
     */
    public Map<VehicleType, List<Vehicle>> getAvailableVehicles(UUID storeId) {
        Store store = system.getStoreById(storeId);
        return store.getAvailableVehicles();
    }

    /**
     * Get all vehicles at a store (available and unavailable).
     */
    public Map<VehicleType, List<Vehicle>> getAllVehicles(UUID storeId) {
        Store store = system.getStoreById(storeId);
        Map<VehicleType, List<Vehicle>> available = store.getAvailableVehicles();
        Map<VehicleType, List<Vehicle>> unavailable = store.getUnavailableVehicles();

        // Merge both maps
        unavailable.forEach((type, vehicles) -> available.merge(type, vehicles, (v1, v2) -> {
            List<Vehicle> merged = new ArrayList<>(v1);
            merged.addAll(v2);
            return merged;
        }));

        return available;
    }

    /**
     * Get a specific vehicle by ID.
     */
    public Vehicle getVehicleById(UUID storeId, UUID vehicleId) {
        Store store = system.getStoreById(storeId);
        return store.getVehicleById(vehicleId);
    }

    /**
     * Get the default daily rate for a vehicle type.
     * Useful for displaying pricing information.
     */
    public double getDefaultDailyRate(VehicleType type) {
        return VehicleFactory.getDefaultDailyRate(type);
    }

    /**
     * Search vehicles using the Specification Pattern.
     * Allows multi-criteria search with dynamic filtering.
     * 
     * This implementation uses the Specification Pattern for:
     * - Clean, composable filter logic
     * - O(log n) lookup potential with indexed structures
     * - Easy addition of new filter criteria
     * 
     * @param storeId Store to search in
     * @param spec    The specification to filter by
     * @return List of vehicles matching the specification
     */
    public List<Vehicle> searchVehicles(UUID storeId, vehiclerentalsystem.specification.Specification<Vehicle> spec) {
        Store store = system.getStoreById(storeId);

        // Get all vehicles and filter using the specification
        List<Vehicle> allVehicles = new ArrayList<>();
        store.getInventory().getAllVehicle().values().forEach(inv -> allVehicles.addAll(inv.getAllVehicles()));

        // Apply specification filter - O(n) scan, but can be optimized with indexes
        return allVehicles.stream()
                .filter(spec::isSatisfiedBy)
                .toList();
    }

    /**
     * Search vehicles using builder parameters.
     * Convenience method that builds specification from individual criteria.
     * 
     * @param storeId  Store to search in
     * @param type     Vehicle type filter (optional)
     * @param status   Vehicle status filter (optional)
     * @param minPrice Minimum daily rate filter (optional)
     * @param maxPrice Maximum daily rate filter (optional)
     * @return List of vehicles matching all criteria
     */
    public List<Vehicle> searchVehicles(UUID storeId, VehicleType type, VehicleStatus status,
            Double minPrice, Double maxPrice) {
        vehiclerentalsystem.specification.Specification<Vehicle> spec = vehiclerentalsystem.specification.vehicle.VehicleSpecificationBuilder
                .builder()
                .withType(type)
                .withStatus(status)
                .withPriceRange(minPrice, maxPrice)
                .build();

        return searchVehicles(storeId, spec);
    }
}
