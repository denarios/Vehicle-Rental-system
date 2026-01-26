package vehiclerentalsystem.services;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import vehiclerentalsystem.enums.ReservationStatus;
import vehiclerentalsystem.lock.LockService;
import vehiclerentalsystem.lock.LockTemplate;
import vehiclerentalsystem.model.Reservation;
import vehiclerentalsystem.model.Store;
import vehiclerentalsystem.model.User;
import vehiclerentalsystem.model.Vehicle;
import vehiclerentalsystem.model.VehicleRentalSystem;

/**
 * Service layer for reservation management.
 * 
 * Uses Distributed Locking to prevent double-booking:
 * - When creating a reservation, we lock the vehicle first
 * - This ensures only one thread can book a specific vehicle at a time
 * - Lock is automatically released after booking completes or fails
 * 
 * Uses Observer Pattern for event notifications:
 * - Publishes events when reservations are created/cancelled/completed
 * - Observers (Email, Analytics, Logging) are notified automatically
 * 
 * Interview points:
 * - "Implemented distributed locking to prevent race conditions"
 * - "Used Observer Pattern for event-driven notifications"
 */
@Service
public class ReservationService {

    private final VehicleRentalSystem system;
    private final LockService lockService;
    private final vehiclerentalsystem.observer.EventPublisher eventPublisher;

    // Lock timeout for vehicle reservations
    private static final long LOCK_TIMEOUT_SECONDS = 5;

    public ReservationService(VehicleRentalSystem system, LockService lockService,
            vehiclerentalsystem.observer.EventPublisher eventPublisher) {
        this.system = system;
        this.lockService = lockService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Create a reservation with distributed locking.
     * 
     * The lock ensures that if two users try to book the same vehicle
     * at the same time, only one will succeed.
     * 
     * Flow:
     * 1. Validate inputs
     * 2. ACQUIRE LOCK on vehicle
     * 3. Check if vehicle is available
     * 4. Create reservation
     * 5. RELEASE LOCK
     */
    public Reservation createReservation(UUID storeId, UUID userId, UUID vehicleId,
            LocalDate fromDate, LocalDate toDate) {

        // Validations (before acquiring lock to fail fast)
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

        // Check if user has driving licence (before locking)
        if (!user.hasDrivingLicence()) {
            throw new IllegalStateException("User must have a valid driving licence to make a reservation");
        }

        // ==================== DISTRIBUTED LOCKING ====================
        // Lock the vehicle to prevent double-booking
        // Only one thread can hold this lock at a time
        Reservation reservation = LockTemplate.executeWithLock(
                lockService,
                vehicleId,
                LOCK_TIMEOUT_SECONDS,
                TimeUnit.SECONDS,
                () -> {
                    // This code runs while holding the lock on the vehicle
                    Vehicle vehicle = store.getVehicleById(vehicleId);

                    // Check vehicle availability (inside the lock!)
                    if (!store.isVehicleAvailable(vehicle)) {
                        throw new IllegalStateException(
                                "Vehicle is not available for booking. " +
                                        "It may have been booked by another user.");
                    }

                    return store.reserveVehicle(user, vehicle, fromDate, toDate);
                });
        // Lock is automatically released here
        // ==============================================================

        // ==================== OBSERVER PATTERN ====================
        // Publish event to notify all observers
        vehiclerentalsystem.observer.ReservationEvent event = vehiclerentalsystem.observer.ReservationEvent.created(
                reservation.getReservationId(),
                userId,
                vehicleId,
                storeId,
                fromDate,
                toDate,
                reservation.getTotalPrice());
        eventPublisher.publish(event);
        // ==========================================================

        return reservation;
    }

    public List<Reservation> getReservationsByStore(UUID storeId) {
        Store store = system.getStoreById(storeId);
        return store.getReservations();
    }

    public List<Reservation> getReservationsByUser(UUID userId) {
        system.getUserById(userId);
        return system.getAllStores().stream()
                .flatMap(store -> store.getReservations().stream())
                .filter(r -> r.getUser().getId().equals(userId))
                .toList();
    }

    /**
     * Cancel a reservation with locking.
     * Locks the vehicle to prevent conflicts with new bookings.
     */
    public Reservation cancelReservation(UUID storeId, UUID reservationId) {
        Store store = system.getStoreById(storeId);
        Reservation reservation = store.getReservationById(reservationId);
        UUID vehicleId = reservation.getVehicle().getId();

        // Lock the vehicle during cancellation
        Reservation result = LockTemplate.executeWithLock(
                lockService,
                vehicleId,
                LOCK_TIMEOUT_SECONDS,
                TimeUnit.SECONDS,
                () -> {
                    if (reservation.getStatus() == ReservationStatus.CANCELLED) {
                        throw new IllegalStateException("Reservation is already cancelled");
                    }
                    if (reservation.getStatus() == ReservationStatus.COMPLETED) {
                        throw new IllegalStateException("Cannot cancel a completed reservation");
                    }

                    reservation.setStatus(ReservationStatus.CANCELLED);
                    return reservation;
                });

        // Publish cancellation event
        vehiclerentalsystem.observer.ReservationEvent event = vehiclerentalsystem.observer.ReservationEvent.cancelled(
                reservation.getReservationId(),
                reservation.getUser().getId(),
                vehicleId,
                storeId,
                reservation.getFromDate(),
                reservation.getToDate(),
                reservation.getTotalPrice());
        eventPublisher.publish(event);

        return result;
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

        // Publish completion event
        vehiclerentalsystem.observer.ReservationEvent event = vehiclerentalsystem.observer.ReservationEvent.completed(
                reservation.getReservationId(),
                reservation.getUser().getId(),
                reservation.getVehicle().getId(),
                storeId,
                reservation.getFromDate(),
                reservation.getToDate(),
                reservation.getTotalPrice());
        eventPublisher.publish(event);

        return reservation;
    }
}
