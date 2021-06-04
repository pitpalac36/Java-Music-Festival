package festival.server;

import festival.domain.models.Artist;
import festival.domain.models.Show;
import festival.domain.models.Ticket;
import festival.domain.models.User;
import festival.persistance.IShowRepository;
import festival.persistance.ITicketRepository;
import festival.persistance.IUserRepository;
import festival.services.INotificationService;
import festival.services.IServicesAMS;
import festival.validator.InvalidPurchaseException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IServicesAMS {
    private IUserRepository userRepository;
    private ITicketRepository ticketRepository;
    private IShowRepository showRepository;
    private Map<String, User> loggedClients;
    private INotificationService notificationService;

    public Service(IUserRepository userRepo, ITicketRepository ticketRepo, IShowRepository showRepo, INotificationService notificationService) {
        userRepository = userRepo;
        ticketRepository = ticketRepo;
        showRepository = showRepo;
        loggedClients=new ConcurrentHashMap<>();
        this.notificationService = notificationService;
        System.out.println("repos were initialized!");
        System.out.println("userRepo : " + userRepo.findAll().size() + " users");
    }

    @Override
    public synchronized void login(User user) throws java.lang.Error {
        if (userRepository.findOne(user.getUsername(), user.getPassword())) {
            if (loggedClients.get(user.getUsername()) != null) {
                throw new java.lang.Error("User already logged in!");
            }
            loggedClients.put(user.getUsername(), user);
        } else {
            throw new java.lang.Error("Authentication failed!");
        }
        userRepository.disconnect();
    }

    @Override
    public void logout(User user) throws java.lang.Error {
        User localClient = loggedClients.remove(user.getUsername());
        if (localClient == null) {
            throw new java.lang.Error("User " + user.getUsername() + " was not logged in!");
        }
    }

    @Override
    public synchronized Show[] getAll() {
        List<Show> shows =  showRepository.findAll();
        showRepository.disconnect();
        return shows.toArray(new Show[shows.size()]);
    }

    @Override
    public synchronized Artist[] getArtists(String date) throws java.lang.Error {
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
                        notificationService.newSell(ticket);
                        System.out.println("Notifying " + x);
                    } catch (java.lang.Error error) {
                        System.err.println("festival.services.Error notifying user :( " + error);
                    }
                });
        }
        showRepository.disconnect();
        ticketRepository.disconnect();
    }
}
