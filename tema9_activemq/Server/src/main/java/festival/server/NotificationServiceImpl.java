package festival.server;
import festival.domain.models.Ticket;
import festival.domain.notification.Notification;
import festival.domain.notification.NotificationType;
import festival.services.INotificationService;
import org.springframework.jms.core.JmsOperations;

public class NotificationServiceImpl implements INotificationService {

    private JmsOperations jmsOperations;

    public NotificationServiceImpl(JmsOperations operations) {
        jmsOperations=operations;
    }

    @Override
    public void newSell(Ticket ticket) {
        System.out.println("New sell notification");
        Notification notif=new Notification(NotificationType.TICKET_SOLD, ticket);
        jmsOperations.convertAndSend(notif);
        System.out.println("Sent ticket to ActiveMQ... " + notif);
    }
}
