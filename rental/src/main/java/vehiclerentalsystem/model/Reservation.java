package vehiclerentalsystem.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import vehiclerentalsystem.enums.ReservationStatus;

/**
 * Represents a vehicle reservation in the rental system.
 * 
 * Enhanced with Strategy Pattern integration:
 * - Stores the total price calculated using a pricing strategy
 * - Tracks which pricing strategy was used for transparency
 */
public class Reservation {

    private UUID reservationId;
    private User user;
    private Vehicle vehicle;
    private LocalDate fromDate;
    private LocalDate toDate;
    private ReservationStatus status;
    private double totalPrice;
    private String pricingStrategy;

    /**
     * Legacy constructor for backward compatibility.
     * Uses default pricing (no strategy applied).
     */
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

        // Calculate default price
        long days = ChronoUnit.DAYS.between(fromDate, toDate) + 1;
        this.totalPrice = vehicle.getDailyRate() * days;
        this.pricingStrategy = "STANDARD";
    }

    /**
     * Full constructor with pricing strategy support.
     * 
     * @param user            The user making the reservation
     * @param vehicle         The vehicle being reserved
     * @param fromDate        Start date of the reservation
     * @param toDate          End date of the reservation
     * @param totalPrice      The calculated total price
     * @param pricingStrategy The name of the pricing strategy used
     */
    public Reservation(User user,
            Vehicle vehicle,
            LocalDate fromDate,
            LocalDate toDate,
            double totalPrice,
            String pricingStrategy) {

        this.reservationId = UUID.randomUUID();
        this.user = user;
        this.vehicle = vehicle;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = ReservationStatus.ACTIVE;
        this.totalPrice = totalPrice;
        this.pricingStrategy = pricingStrategy;
    }

    // =============== Getters ===============

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

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getPricingStrategy() {
        return pricingStrategy;
    }

    /**
     * Get the number of rental days.
     * 
     * @return Number of days (inclusive of both start and end dates)
     */
    public long getRentalDays() {
        return ChronoUnit.DAYS.between(fromDate, toDate) + 1;
    }

    // =============== Setters ===============

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPricingStrategy(String pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    // =============== Utility Methods ===============

    /** Reservation active for a given date */
    public boolean isActive(LocalDate date) {
        return status == ReservationStatus.ACTIVE &&
                !date.isBefore(fromDate) && !date.isAfter(toDate);
    }

    @Override
    public String toString() {
        return String.format("Reservation[id=%s, user=%s, vehicle=%s, %s to %s, $%.2f (%s)]",
                reservationId, user.getName(), vehicle.getType(),
                fromDate, toDate, totalPrice, pricingStrategy);
    }
}
