package vehiclerentalsystem.services;

import org.springframework.stereotype.Service;
import vehiclerentalsystem.enums.ReservationStatus;
import vehiclerentalsystem.model.Reservation;
import vehiclerentalsystem.model.Store;
import vehiclerentalsystem.model.User;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.model.VehicleRentalSystem;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final VehicleRentalSystem system;

    public ReservationService(VehicleRentalSystem system) {
        this.system = system;
    }

    public Reservation createReservation(UUID storeId, UUID userId, UUID vehicleId,
            LocalDate fromDate, LocalDate toDate) {
        // Validations
        if (fromDate == null || toDate == null) {
            throw new IllegalArgumentException("Dates cannot be null");
        }
        if (fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException("From date cannot be after to date");
        }
        if (fromDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot create reservation for past dates");
        }

        Store store = system.getStoreById(storeId);
        User user = system.getUserById(userId);
        Vehicle vehicle = store.getVehicleById(vehicleId);

        // Check if user has driving licence
        if (!user.hasDrivingLicence()) {
            throw new IllegalStateException("User must have a valid driving licence to make a reservation");
        }

        return store.reserveVehicle(user, vehicle, fromDate, toDate);
    }

    public List<Reservation> getReservationsByStore(UUID storeId) {
        Store store = system.getStoreById(storeId);
        return store.getReservations();
    }

    public List<Reservation> getReservationsByUser(UUID userId) {
        // Validate user exists
        system.getUserById(userId);
        return system.getAllStores().stream()
                .flatMap(store -> store.getReservations().stream())
                .filter(r -> r.getUser().getId().equals(userId))
                .toList();
    }

    public Reservation cancelReservation(UUID storeId, UUID reservationId) {
        Store store = system.getStoreById(storeId);
        Reservation reservation = store.getReservationById(reservationId);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Reservation is already cancelled");
        }
        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new IllegalStateException("Cannot cancel a completed reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        return reservation;
    }

    public Reservation completeReservation(UUID storeId, UUID reservationId) {
        Store store = system.getStoreById(storeId);
        Reservation reservation = store.getReservationById(reservationId);

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Cannot complete a cancelled reservation");
        }
        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new IllegalStateException("Reservation is already completed");
        }

        reservation.setStatus(ReservationStatus.COMPLETED);
        return reservation;
    }
}
