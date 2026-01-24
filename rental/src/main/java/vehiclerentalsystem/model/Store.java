package vehiclerentalsystem.model;

import java.time.LocalDate;
import java.util.*;

import vehiclerentalsystem.enums.ReservationStatus;
import vehiclerentalsystem.enums.VehicleStatus;
import vehiclerentalsystem.enums.VehicleType;

public class Store {

    private Inventory inventory;
    private Location location;
    private List<Reservation> reservationList;
    private UUID storeId;

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
    // 2. Vehicle ID not present in active reservations
    public boolean isVehicleAvailable(Vehicle vehicle) {

        if (vehicle.getStatus() != VehicleStatus.ACTIVE) {
            return false;
        }

        return reservationList.stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .noneMatch(r -> r.getVehicle().getId().equals(vehicle.getId()));
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
            throw new IllegalStateException("Vehicle is not available for reservation");
        }

        Reservation reservation = new Reservation(user, vehicle, fromDate, toDate);

        reservationList.add(reservation);
        return reservation;
    }

    public List<Reservation> getReservations() {
        return new ArrayList<>(reservationList);
    }

    public Reservation getReservationById(UUID reservationId) {
        return reservationList.stream()
                .filter(r -> r.getReservationId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Reservation not found: " + reservationId));
    }

    /* ---------------- Getters ---------------- */

    public UUID getStoreId() {
        return storeId;
    }

    public Location getLocation() {
        return location;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Vehicle getVehicleById(UUID vehicleId) {

        return inventory.getAllVehicle().values().stream()
                .flatMap(inv -> inv.getAllVehicles().stream())
                .filter(v -> v.getId().equals(vehicleId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Vehicle not found: " + vehicleId));
    }
}
