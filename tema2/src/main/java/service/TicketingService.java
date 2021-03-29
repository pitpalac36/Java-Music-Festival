package service;
import domain.Show;
import domain.ShowDTO;
import domain.Ticket;
import repository.IShowRepository;
import repository.ITicketRepository;
import validator.InvalidPurchaseException;
import java.util.List;

public class TicketingService {

    private IShowRepository showRepo;
    private ITicketRepository ticketRepo;

    public TicketingService(IShowRepository showRepo, ITicketRepository ticketRepo) {
        this.showRepo = showRepo;
        this.ticketRepo = ticketRepo;
    }

    public List<Show> getAll() {
        List<Show> all = showRepo.findAll();
        showRepo.disconnect();
        return all;
    }

    public List<ShowDTO> getArtists(String date) {
        List<ShowDTO> artists = showRepo.findArtists(date);
        showRepo.disconnect();
        return artists;
    }

    public void buyTickets(int showId, int quantity, String purchaserName) throws InvalidPurchaseException {
        Show show = showRepo.findOne(showId);
        if (show.getAvailableTicketsNumber() < quantity) {
            throw new InvalidPurchaseException(quantity, showId);
        } else {
            ticketRepo.save(new Ticket(showId, purchaserName, quantity));
            show.setAvailableTicketsNumber(show.getAvailableTicketsNumber() - quantity);
            show.setSoldTicketsNumber(show.getSoldTicketsNumber() + quantity);
            showRepo.update(show);
        }
        showRepo.disconnect();
        ticketRepo.disconnect();
    }
}
