package vehiclerentalsystem.observer;

import org.springframework.stereotype.Component;

/**
 * Analytics Observer.
 * 
 * Tracks metrics and analytics for business intelligence.
 * 
 * In production, this would send data to:
 * - Google Analytics
 * - Mixpanel
 * - Custom analytics dashboard
 * - Data warehouse
 */
@Component
public class AnalyticsObserver implements EventObserver {

    private int reservationsCreated = 0;
    private int reservationsCancelled = 0;
    private int reservationsCompleted = 0;
    private double totalRevenue = 0.0;

    @Override
    public void onEvent(Event event) {
        if (event instanceof ReservationEvent) {
            ReservationEvent resEvent = (ReservationEvent) event;
            trackEvent(resEvent);
        }
    }

    private void trackEvent(ReservationEvent event) {
        switch (event.getEventType()) {
            case "RESERVATION_CREATED":
                reservationsCreated++;
                System.out.println("📊 [ANALYTICS] Reservation created");
                System.out.println("   Total reservations: " + reservationsCreated);
                break;

            case "RESERVATION_CANCELLED":
                reservationsCancelled++;
                System.out.println("📊 [ANALYTICS] Reservation cancelled");
                System.out.println("   Cancellation rate: " +
                        String.format("%.1f%%", getCancellationRate()));
                break;

            case "RESERVATION_COMPLETED":
                reservationsCompleted++;
                totalRevenue += event.getTotalPrice();
                System.out.println("📊 [ANALYTICS] Reservation completed");
                System.out.println("   Total revenue: $" + String.format("%.2f", totalRevenue));
                System.out.println("   Completed reservations: " + reservationsCompleted);
                break;
        }
    }

    private double getCancellationRate() {
        int total = reservationsCreated;
        if (total == 0)
            return 0.0;
        return (reservationsCancelled * 100.0) / total;
    }

    @Override
    public String getObserverName() {
        return "AnalyticsObserver";
    }

    // Getters for metrics (useful for dashboard/reporting)
    public int getReservationsCreated() {
        return reservationsCreated;
    }

    public int getReservationsCancelled() {
        return reservationsCancelled;
    }

    public int getReservationsCompleted() {
        return reservationsCompleted;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }
}
