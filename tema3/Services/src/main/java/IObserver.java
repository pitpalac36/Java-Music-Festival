import domain.Ticket;

public interface IObserver {

    void ticketSold(Ticket ticket) throws Error;
}
