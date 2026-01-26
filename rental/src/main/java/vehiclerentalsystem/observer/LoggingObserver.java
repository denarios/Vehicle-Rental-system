package vehiclerentalsystem.observer;

import org.springframework.stereotype.Component;

/**
 * Logging Observer.
 * 
 * Logs all events to console/file for audit trail.
 * 
 * In production, this would write to:
 * - Log files (using Logback/Log4j)
 * - Centralized logging (ELK stack, Splunk)
 * - Audit database
 */
@Component
public class LoggingObserver implements EventObserver {

    @Override
    public void onEvent(Event event) {
        logEvent(event);
    }

    private void logEvent(Event event) {
        System.out.println("📝 [AUDIT LOG] " + event.toString());

        if (event instanceof ReservationEvent) {
            ReservationEvent resEvent = (ReservationEvent) event;
            System.out.println("   Details: User=" + resEvent.getUserId() +
                    ", Vehicle=" + resEvent.getVehicleId() +
                    ", Store=" + resEvent.getStoreId() +
                    ", Price=$" + resEvent.getTotalPrice());
        }
    }

    @Override
    public String getObserverName() {
        return "LoggingObserver";
    }
}
