import domain.Ticket;

public interface ITicketRepository extends IRepository<Ticket> {
    void save(Ticket ticket);
}