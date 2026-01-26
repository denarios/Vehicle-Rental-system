package vehiclerentalsystem.services;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.Store;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.model.VehicleRentalSystem;

/**
 * Service layer for store management.
 * 
 * Uses Redis caching for:
 * - getAllStores() - Cached for 30 minutes
 * - getStoreById() - Cached per store ID
 * 
 * Cache is invalidated when stores are created/modified.
 */
@Service
public class StoreService {

    private final VehicleRentalSystem system;

    public StoreService(VehicleRentalSystem system) {
        this.system = system;
    }

    /**
     * Create a new store.
     * Evicts the "stores" cache to ensure fresh data.
     */
    @CacheEvict(value = "stores", allEntries = true)
    public Store createStore(String state, String district, String pincode) {
        if (state == null || state.trim().isEmpty()) {
            throw new IllegalArgumentException("State cannot be empty");
        }
        if (district == null || district.trim().isEmpty()) {
            throw new IllegalArgumentException("District cannot be empty");
        }
        if (pincode == null || pincode.trim().isEmpty()) {
            throw new IllegalArgumentException("Pincode cannot be empty");
        }

        Store store = new Store(state, district, pincode);
        system.addStore(store);
        return store;
    }

    /**
     * Get all stores.
     * Results are cached for 30 minutes (configured in RedisConfig).
     */
    @Cacheable(value = "stores", key = "'all'")
    public List<Store> getAllStores() {
        return system.getAllStores();
    }

    /**
     * Get store by ID.
     * Results are cached per store ID.
     */
    @Cacheable(value = "stores", key = "#storeId")
    public Store getStoreById(UUID storeId) {
        return system.getStoreById(storeId);
    }

    public Map<VehicleType, List<Vehicle>> getAvailableVehicles(UUID storeId) {
        Store store = system.getStoreById(storeId);
        return store.getAvailableVehicles();
    }

    public Map<VehicleType, List<Vehicle>> getUnavailableVehicles(UUID storeId) {
        Store store = system.getStoreById(storeId);
        return store.getUnavailableVehicles();
    }
}
