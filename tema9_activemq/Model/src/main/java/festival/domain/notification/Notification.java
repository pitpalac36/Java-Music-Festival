package festival.domain.notification;

import festival.domain.models.Ticket;

public class Notification {
    private NotificationType type;
    private Ticket ticketSold;

    public Notification(NotificationType type, Ticket ticketSold) {
        this.type = type;
        this.ticketSold = ticketSold;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Ticket getTicketSold() {
        return ticketSold;
    }

    public void setTicketSold(Ticket ticketSold) {
        this.ticketSold = ticketSold;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "type=" + type +
                ", ticketSold=" + ticketSold +
                '}';
    }
}
