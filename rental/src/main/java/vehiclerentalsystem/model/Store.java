package vehiclerentalsystem.model;

import java.time.LocalDate;
import java.util.*;

import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;

public class Store {

    private final Inventory inventory;
    private final Location location;
    private final List<Reservation> reservationList;
    private final UUID storeId;

    public Store(String state, String district, String pincode) {
        this.inventory = new Inventory();
        this.location = new Location(state, district, pincode);
        this.reservationList = new ArrayList<>();
        this.storeId = UUID.randomUUID();
    }

    /* ---------------- Vehicle Management ---------------- */

    public void addVehicle(Vehicle vehicle) {
        inventory.addVehicle(vehicle);
    }

    /* ---------------- Availability Logic ---------------- */

    // Vehicle is available if:
    // 1. Vehicle is ACTIVE (not damaged)
    // 2. Vehicle ID not present in reservation list
    public boolean isVehicleAvailable(Vehicle vehicle) {

        if (vehicle.getStatus() != VehicleStatus.ACTIVE) {
            return false;
        }

        return reservationList.stream()
                .noneMatch(r ->
                        r.getVehicle().getId().equals(vehicle.getId())
                );
    }

    public Map<VehicleType, List<Vehicle>> getAvailableVehicles() {

        Map<VehicleType, List<Vehicle>> result = new HashMap<>();

        inventory.getAllVehicle().forEach((type, inv) -> {

            List<Vehicle> availableVehicles = inv.getAllVehicles().stream()
                    .filter(this::isVehicleAvailable)
                    .toList();

            if (!availableVehicles.isEmpty()) {
                result.put(type, availableVehicles);
            }
        });

        return result;
    }

    public Map<VehicleType, List<Vehicle>> getUnavailableVehicles() {

        Map<VehicleType, List<Vehicle>> result = new HashMap<>();

        inventory.getAllVehicle().forEach((type, inv) -> {

            List<Vehicle> unavailableVehicles = inv.getAllVehicles().stream()
                    .filter(v -> !isVehicleAvailable(v))
                    .toList();

            if (!unavailableVehicles.isEmpty()) {
                result.put(type, unavailableVehicles);
            }
        });

        return result;
    }

    /* ---------------- Reservation Management ---------------- */

    public Reservation reserveVehicle(User user,
                                      Vehicle vehicle,
                                      LocalDate fromDate,
                                      LocalDate toDate) {

        if (!isVehicleAvailable(vehicle)) {
            throw new IllegalStateException("Vehicle is not available");
        }

        Reservation reservation =
                new Reservation(user, vehicle, fromDate, toDate);

        reservationList.add(reservation);
        return reservation;
    }

    /* ---------------- Cleanup (Cron / Scheduler) ---------------- */

    /* ---------------- Getters ---------------- */

    public UUID getStoreId() {
        return storeId;
    }

    public Location getLocation() {
        return location;
    }
}
