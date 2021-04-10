import domain.Show;
import domain.Artist;
import domain.Ticket;
import domain.User;
import validator.InvalidPurchaseException;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IService {
    private IUserRepository userRepository;
    private ITicketRepository ticketRepository;
    private IShowRepository showRepository;
    private Map<String, IObserver> loggedClients;

    public Service(IUserRepository userRepo, ITicketRepository ticketRepo, IShowRepository showRepo) {
        userRepository = userRepo;
        ticketRepository = ticketRepo;
        showRepository = showRepo;
        loggedClients = new ConcurrentHashMap<>();
        System.out.println("repos were initialized!");
        System.out.println("userRepo : " + userRepo.findAll().size() + " users");
    }

    @Override
    public synchronized void login(User user, IObserver client) throws Error {
        if (userRepository.findOne(user.getUsername(), user.getPassword())) {
            if (loggedClients.get(user.getUsername()) != null) {
                throw new Error("User already logged in!");
            }
            loggedClients.put(user.getUsername(), client);
        } else {
            throw new Error("Authentication failed!");
        }
        userRepository.disconnect();
    }

    @Override
    public void logout(User user, IObserver client) throws Error {
        IObserver localClient = loggedClients.remove(user.getUsername());
        if (localClient == null) {
            throw new Error("User " + user.getUsername() + " was not logged in!");
        }
    }

    @Override
    public synchronized Show[] getAll() {
        List<Show> shows =  showRepository.findAll();
        showRepository.disconnect();
        return shows.toArray(new Show[shows.size()]);
    }

    @Override
    public synchronized Artist[] getArtists(String date) throws Error {
        List<Artist> artists =  showRepository.findArtists(date);
        showRepository.disconnect();
        return artists.toArray(new Artist[artists.size()]);
    }

    @Override
    public void sellTickets(Ticket ticket) throws InvalidPurchaseException {
        Show show = showRepository.findOne(ticket.getShowId());
        if (show.getAvailableTicketsNumber() < ticket.getNumber()) {
            throw new InvalidPurchaseException(ticket.getNumber(), ticket.getShowId());
        } else {
            ticketRepository.save(ticket);
            show.setAvailableTicketsNumber(show.getAvailableTicketsNumber() - ticket.getNumber());
            show.setSoldTicketsNumber(show.getSoldTicketsNumber() + ticket.getNumber());
            showRepository.update(show);
            System.out.println("Notifying others about the sell");
            loggedClients.values().forEach(x -> {
                    try {
                        x.ticketSold(ticket);
                        System.out.println("Notifying " + x);
                    } catch (Error | RemoteException error) {
                        System.err.println("Error notifying user :( " + error);
                    }
                });
        }
        showRepository.disconnect();
        ticketRepository.disconnect();
    }
}
