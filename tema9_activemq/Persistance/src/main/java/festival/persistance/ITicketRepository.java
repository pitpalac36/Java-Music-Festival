package festival.persistance;

import festival.domain.models.Ticket;

public interface ITicketRepository extends IRepository<Ticket> {
    void save(Ticket ticket);
}