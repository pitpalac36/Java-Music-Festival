package festival.services;

import festival.model.domain.Ticket;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IObserver extends Remote {

    void ticketSold(Ticket ticket) throws RemoteException, Error;
}
