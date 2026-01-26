package vehiclerentalsystem.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vehiclerentalsystem.model.Store;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.model.VehicleRentalSystem;

/**
 * Vehicle search service with location-based filtering.
 */
@Service
public class VehicleSearchService {

    private final VehicleRentalSystem system;

    public VehicleSearchService(VehicleRentalSystem system) {
        this.system = system;
    }

    /**
     * Search vehicles by city and state.
     * Returns all vehicles from stores matching the location.
     */
    public List<Vehicle> searchByLocation(String city, String state) {
        return system.getAllStores().stream()
                .filter(store -> matchesLocation(store, city, state))
                .flatMap(store -> getAllVehiclesFromStore(store).stream())
                .collect(Collectors.toList());
    }

    /**
     * Search vehicles by city only.
     */
    public List<Vehicle> searchByCity(String city) {
        return system.getAllStores().stream()
                .filter(store -> store.getLocation().getCity().equalsIgnoreCase(city))
                .flatMap(store -> getAllVehiclesFromStore(store).stream())
                .collect(Collectors.toList());
    }

    private boolean matchesLocation(Store store, String city, String state) {
        boolean matchesCity = city == null || city.isEmpty() ||
                store.getLocation().getCity().equalsIgnoreCase(city);
        boolean matchesState = state == null || state.isEmpty() ||
                store.getLocation().getState().equalsIgnoreCase(state);
        return matchesCity && matchesState;
    }

    private List<Vehicle> getAllVehiclesFromStore(Store store) {
        return store.getInventory().getAllVehicle().values().stream()
                .flatMap(inv -> inv.getAllVehicles().stream())
                .collect(Collectors.toList());
    }
}
