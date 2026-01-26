package vehiclerentalsystem.config;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import vehiclerentalsystem.observer.AnalyticsObserver;
import vehiclerentalsystem.observer.EmailNotificationObserver;
import vehiclerentalsystem.observer.EventPublisher;
import vehiclerentalsystem.observer.LoggingObserver;

/**
 * Observer Pattern Configuration.
 * 
 * Registers all observers with the EventPublisher on application startup.
 * 
 * This is where you wire up the Observer Pattern:
 * - Subject (EventPublisher) is created
 * - Observers (Email, Analytics, Logging) are registered
 */
@Configuration
public class ObserverConfig {

    private final EventPublisher eventPublisher;
    private final EmailNotificationObserver emailObserver;
    private final AnalyticsObserver analyticsObserver;
    private final LoggingObserver loggingObserver;

    public ObserverConfig(EventPublisher eventPublisher,
            EmailNotificationObserver emailObserver,
            AnalyticsObserver analyticsObserver,
            LoggingObserver loggingObserver) {
        this.eventPublisher = eventPublisher;
        this.emailObserver = emailObserver;
        this.analyticsObserver = analyticsObserver;
        this.loggingObserver = loggingObserver;
    }

    /**
     * Register all observers when the application starts.
     * 
     * @PostConstruct runs after Spring creates all beans.
     */
    @PostConstruct
    public void registerObservers() {
        System.out.println("=".repeat(60));
        System.out.println("🔔 Registering Event Observers (Observer Pattern)");
        System.out.println("=".repeat(60));

        eventPublisher.subscribe(loggingObserver);
        eventPublisher.subscribe(emailObserver);
        eventPublisher.subscribe(analyticsObserver);

        System.out.println("✅ Registered " + eventPublisher.getObserverCount() + " observers:");
        for (String name : eventPublisher.getObserverNames()) {
            System.out.println("   - " + name);
        }
        System.out.println("=".repeat(60));
    }
}
