package vehiclerentalsystem.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import vehiclerentalsystem.enums.VehicleType;

public class VehicleRentalSystem {

    private  List<User> users;
    private  List<Store> stores;

    public VehicleRentalSystem() {
        this.users = new ArrayList<>();
        this.stores = new ArrayList<>();
    }

    /* -------- User Management -------- */

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getAllUsers() {
        return users;
    }

    /* -------- Store Management -------- */

    public void addStore(Store store) {
        stores.add(store);
    }

    public List<Store> getAllStores() {
        return stores;
    }

    /* -------- Search -------- */
    public Store getStoreById(UUID storeId) {
        return stores.stream()
                .filter(store -> store.getStoreId().equals(storeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Store not found"));
    }

    public User getUserById(UUID userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void showAvailableVehiclesAcrossStores() {
        for (Store store : stores) {
            System.out.println("Store ID: " + store.getStoreId());
            Map<VehicleType, List<Vehicle>> available = store.getAvailableVehicles();

            if (available.isEmpty()) {
                System.out.println("  No vehicles available");
            } else {
                available.forEach((type, vehicles) -> {
                    System.out.println("  " + type + " -> " + vehicles.size());
                });
            }
        }
    }
}
