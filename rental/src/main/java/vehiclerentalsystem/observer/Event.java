package vehiclerentalsystem.observer;

import java.time.LocalDateTime;
import java.util.UUID;

import vehiclerentalsystem.enums.ReservationStatus;

/**
 * Base class for all events in the system.
 * 
 * Events represent things that have happened:
 * - Reservation created
 * - Reservation cancelled
 * - Vehicle added
 * - etc.
 */
public abstract class Event {

    private final UUID eventId;
    private final LocalDateTime timestamp;
    private final String eventType;

    protected Event(String eventType) {
        this.eventId = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.eventType = eventType;
    }

    public UUID getEventId() {
        return eventId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s at %s", eventId, eventType, timestamp);
    }
}
