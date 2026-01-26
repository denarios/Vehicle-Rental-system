package vehiclerentalsystem.observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;

/**
 * Event Publisher - the "Subject" in the Observer Pattern.
 * 
 * Manages a list of observers and notifies them when events occur.
 * 
 * Thread-safe using CopyOnWriteArrayList.
 * 
 * Interview point: "Implemented Observer Pattern for event-driven
 * notifications when reservations are created or modified"
 */
@Component
public class EventPublisher {

    /**
     * List of all registered observers.
     * CopyOnWriteArrayList is thread-safe for concurrent reads/writes.
     */
    private final List<EventObserver> observers = new CopyOnWriteArrayList<>();

    /**
     * Register an observer to receive event notifications.
     * 
     * @param observer The observer to register
     */
    public void subscribe(EventObserver observer) {
        observers.add(observer);
        System.out.println("[EVENT] Observer registered: " + observer.getObserverName());
    }

    /**
     * Unregister an observer.
     * 
     * @param observer The observer to unregister
     */
    public void unsubscribe(EventObserver observer) {
        observers.remove(observer);
        System.out.println("[EVENT] Observer unregistered: " + observer.getObserverName());
    }

    /**
     * Publish an event to all registered observers.
     * Each observer's onEvent() method is called.
     * 
     * @param event The event to publish
     */
    public void publish(Event event) {
        System.out.println("[EVENT] Publishing: " + event);

        for (EventObserver observer : observers) {
            try {
                observer.onEvent(event);
            } catch (Exception e) {
                // Don't let one observer's failure affect others
                System.err.println("[EVENT] Error in observer " +
                        observer.getObserverName() + ": " + e.getMessage());
            }
        }

        System.out.println("[EVENT] Notified " + observers.size() + " observer(s)");
    }

    /**
     * Get the number of registered observers.
     */
    public int getObserverCount() {
        return observers.size();
    }

    /**
     * Get list of all observer names (for debugging).
     */
    public List<String> getObserverNames() {
        List<String> names = new ArrayList<>();
        for (EventObserver observer : observers) {
            names.add(observer.getObserverName());
        }
        return names;
    }
}
