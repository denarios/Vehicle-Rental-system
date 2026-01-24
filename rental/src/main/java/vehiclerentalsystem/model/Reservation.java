package vehiclerentalsystem.model;

import java.time.LocalDate;
import java.util.UUID;

import vehiclerentalsystem.enums.ReservationStatus;

public class Reservation {

    private UUID reservationId;
    private User user;
    private Vehicle vehicle;
    private LocalDate fromDate;
    private LocalDate toDate;
    private ReservationStatus status;

    public Reservation(User user,
            Vehicle vehicle,
            LocalDate fromDate,
            LocalDate toDate) {

        this.reservationId = UUID.randomUUID();
        this.user = user;
        this.vehicle = vehicle;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = ReservationStatus.ACTIVE;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public User getUser() {
        return user;
    }

    public Vehicle getVehicle() {
        return vehicle;
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

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    /** Reservation active for a given date */
    public boolean isActive(LocalDate date) {
        return status == ReservationStatus.ACTIVE &&
                !date.isBefore(fromDate) && !date.isAfter(toDate);
    }
}
