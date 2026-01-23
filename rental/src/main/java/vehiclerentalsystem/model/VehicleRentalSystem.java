package vehiclerentalsystem.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vehiclerentalsystem.enums.VehicleType;
import vehiclerentalsystem.model.Store;
import vehiclerentalsystem.model.User;
import vehiclerentalsystem.model.Vehicle;

public class VehicleRentalSystem {

    private final List<User> users;
    private final List<Store> stores;

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

    public void showAvailableVehiclesAcrossStores() {
        for (Store store : stores) {
            System.out.println("Store ID: " + store.getStoreId());
            Map<VehicleType, List<Vehicle>> available =
                    store.getAvailableVehicles();

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
