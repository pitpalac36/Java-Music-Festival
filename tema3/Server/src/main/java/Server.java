import utils.AbstractServer;
import java.io.IOException;
import java.util.Properties;

public class Server {
    public static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(Server.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set.");
            props.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties : " + e.getMessage());
        }
        IUserRepository userRepository = new UserDbRepository(props);
        ITicketRepository ticketRepository = new TicketDbRepository(props);
        IShowRepository showRepository = new ShowDbRepository(props);
        Service service = new Service(userRepository, ticketRepository, showRepository);

        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(props.getProperty("server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Wrong port number " + e.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Server starting on port " + serverPort);
        AbstractServer server = new ObjectConcurrentServer(serverPort, service);
        try {
            server.start();
        } catch (utils.ServerException e) {
            System.err.println("Error starting the server : " + e.getMessage());
        }
    }
}
