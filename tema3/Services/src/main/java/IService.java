import domain.Artist;
import domain.Show;
import domain.Ticket;
import domain.User;

import java.util.List;

public interface IService {

    void login(User user, IObserver client) throws Error;
    void logout(User user, IObserver client) throws Error;
    Show[] getAll() throws Error;
    Artist[] getArtists(String date) throws Error;
    void sellTickets(Ticket ticket) throws Error, validator.InvalidPurchaseException;
}
