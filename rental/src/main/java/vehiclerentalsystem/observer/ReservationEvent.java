package vehiclerentalsystem.observer;

import java.time.LocalDate;
import java.util.UUID;

import vehiclerentalsystem.enums.ReservationStatus;

/**
 * Event fired when a reservation is created, cancelled, or completed.
 * 
 * Contains all information about the reservation for observers to use.
 */
public class ReservationEvent extends Event {

    private final UUID reservationId;
    private final UUID userId;
    private final UUID vehicleId;
    private final UUID storeId;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final ReservationStatus status;
    private final double totalPrice;

    public ReservationEvent(String eventType, UUID reservationId, UUID userId,
            UUID vehicleId, UUID storeId, LocalDate fromDate,
            LocalDate toDate, ReservationStatus status, double totalPrice) {
        super(eventType);
        this.reservationId = reservationId;
        this.userId = userId;
        this.vehicleId = vehicleId;
        this.storeId = storeId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    // Factory methods for different event types
    public static ReservationEvent created(UUID reservationId, UUID userId, UUID vehicleId,
            UUID storeId, LocalDate fromDate, LocalDate toDate,
            double totalPrice) {
        return new ReservationEvent("RESERVATION_CREATED", reservationId, userId, vehicleId,
                storeId, fromDate, toDate, ReservationStatus.ACTIVE, totalPrice);
    }

    public static ReservationEvent cancelled(UUID reservationId, UUID userId, UUID vehicleId,
            UUID storeId, LocalDate fromDate, LocalDate toDate,
            double totalPrice) {
        return new ReservationEvent("RESERVATION_CANCELLED", reservationId, userId, vehicleId,
                storeId, fromDate, toDate, ReservationStatus.CANCELLED, totalPrice);
    }

    public static ReservationEvent completed(UUID reservationId, UUID userId, UUID vehicleId,
            UUID storeId, LocalDate fromDate, LocalDate toDate,
            double totalPrice) {
        return new ReservationEvent("RESERVATION_COMPLETED", reservationId, userId, vehicleId,
                storeId, fromDate, toDate, ReservationStatus.COMPLETED, totalPrice);
    }

    // Getters
    public UUID getReservationId() {
        return reservationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getVehicleId() {
        return vehicleId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return String.format("%s - Reservation %s for User %s",
                getEventType(), reservationId, userId);
    }
}
