package festival.services;

import festival.model.domain.Artist;
import festival.model.domain.Show;
import festival.model.domain.Ticket;
import festival.model.domain.User;

public interface IService {

    void login(User user, IObserver client) throws Error;
    void logout(User user, IObserver client) throws Error;
    Show[] getAll() throws Error;
    Artist[] getArtists(String date) throws Error;
    void sellTickets(Ticket ticket) throws Error, festival.model.validator.InvalidPurchaseException;
}
