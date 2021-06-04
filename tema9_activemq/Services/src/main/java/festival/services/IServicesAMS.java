package festival.services;

import festival.domain.models.Artist;
import festival.domain.models.Show;
import festival.domain.models.Ticket;
import festival.domain.models.User;

public interface IServicesAMS {

    void login(User user) throws Error;
    void logout(User user) throws Error;
    Show[] getAll() throws Error;
    Artist[] getArtists(String date) throws Error;
    void sellTickets(Ticket ticket) throws Error, festival.validator.InvalidPurchaseException;
}
