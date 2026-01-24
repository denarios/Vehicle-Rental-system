package vehiclerentalsystem.services;

import org.springframework.stereotype.Service;
import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.Store;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.model.VehicleRentalSystem;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class StoreService {

    private final VehicleRentalSystem system;

    public StoreService(VehicleRentalSystem system) {
        this.system = system;
    }

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

    public List<Store> getAllStores() {
        return system.getAllStores();
    }

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
