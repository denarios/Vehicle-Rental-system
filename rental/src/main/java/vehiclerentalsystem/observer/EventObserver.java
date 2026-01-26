package vehiclerentalsystem.observer;

/**
 * Observer interface - the "subscriber" in the Observer Pattern.
 * 
 * Any class that wants to be notified of events must implement this interface.
 * 
 * Examples:
 * - EmailObserver - sends emails when reservations are created
 * - SMSObserver - sends SMS notifications
 * - AnalyticsObserver - tracks metrics
 * - CacheObserver - invalidates cache
 */
public interface EventObserver {

    /**
     * Called when an event occurs.
     * 
     * @param event The event that occurred
     */
    void onEvent(Event event);

    /**
     * Get the name of this observer (for logging/debugging).
     */
    String getObserverName();
}
