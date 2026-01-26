package vehiclerentalsystem.observer;

import org.springframework.stereotype.Component;

/**
 * Email Notification Observer.
 * 
 * Sends email notifications when reservation events occur.
 * 
 * In production, this would integrate with:
 * - SendGrid
 * - AWS SES
 * - JavaMail API
 * 
 * For now, we just log the emails that would be sent.
 */
@Component
public class EmailNotificationObserver implements EventObserver {

    @Override
    public void onEvent(Event event) {
        if (event instanceof ReservationEvent) {
            ReservationEvent resEvent = (ReservationEvent) event;
            sendEmail(resEvent);
        }
    }

    private void sendEmail(ReservationEvent event) {
        switch (event.getEventType()) {
            case "RESERVATION_CREATED":
                System.out.println("📧 [EMAIL] Sending confirmation email to user " + event.getUserId());
                System.out.println("   Subject: Reservation Confirmed!");
                System.out.println("   Body: Your reservation #" + event.getReservationId() +
                        " from " + event.getFromDate() + " to " + event.getToDate());
                System.out.println("   Total: $" + event.getTotalPrice());
                break;

            case "RESERVATION_CANCELLED":
                System.out.println("📧 [EMAIL] Sending cancellation email to user " + event.getUserId());
                System.out.println("   Subject: Reservation Cancelled");
                System.out.println("   Body: Your reservation #" + event.getReservationId() +
                        " has been cancelled.");
                break;

            case "RESERVATION_COMPLETED":
                System.out.println("📧 [EMAIL] Sending completion email to user " + event.getUserId());
                System.out.println("   Subject: Thank you for your rental!");
                System.out.println("   Body: We hope you enjoyed your experience.");
                break;
        }
    }

    @Override
    public String getObserverName() {
        return "EmailNotificationObserver";
    }
}
