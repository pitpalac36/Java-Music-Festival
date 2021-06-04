package festival.networking.objectprotocol.ams;

import festival.services.IServicesAMS;
import festival.networking.utils.AbstractConcurrentServer;
import java.net.Socket;

public class ObjectAMSConcurrentServer extends AbstractConcurrentServer {
    private IServicesAMS server;

    public ObjectAMSConcurrentServer(String port, IServicesAMS server) {
        super(Integer.parseInt(port));
        this.server = server;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientAMSObjectWorker worker=new ClientAMSObjectWorker(server, client);
        Thread tw =new Thread(worker);
        return tw;
    }
}
