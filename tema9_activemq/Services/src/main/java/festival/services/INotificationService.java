package festival.services;

import festival.domain.models.Ticket;

public interface INotificationService {
    void newSell(Ticket ticket);
}
