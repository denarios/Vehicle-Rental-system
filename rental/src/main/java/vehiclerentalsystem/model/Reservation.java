package vehiclerentalsystem.model;

import java.time.LocalDate;
import java.util.UUID;

public class Reservation {

    private final UUID reservationId;
    private final User user;
    private final Vehicle vehicle;
    private final LocalDate fromDate;
    private final LocalDate toDate;

    public Reservation(User user,
                       Vehicle vehicle,
                       LocalDate fromDate,
                       LocalDate toDate) {

        this.reservationId = UUID.randomUUID();
        this.user = user;
        this.vehicle = vehicle;
        this.fromDate = fromDate;
        this.toDate = toDate;
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

    /** Reservation active for a given date */
    public boolean isActive(LocalDate date) {
        return !date.isBefore(fromDate) && !date.isAfter(toDate);
    }
}
