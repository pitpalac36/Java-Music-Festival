import domain.Artist;
import domain.Show;
import domain.Ticket;
import domain.User;
import dtos.*;
import objectprotocol.*;
import validator.InvalidPurchaseException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientObjectWorker implements Runnable, IObserver {
    private IService server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientObjectWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected) {
            try {
                Object request = input.readObject();
                Object response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse((Response) response);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(Response response) throws IOException {
        System.out.println("sending response " + response);
        output.writeObject(response);
        output.flush();
    }

    private Object handleRequest(Request request) {
        Response response = null;

        if (request instanceof LoginRequest) {
            System.out.println("Login request ...");
            LoginRequest loginRequest = (LoginRequest)request;
            UserDto dto = loginRequest.getUser();
            User user= DtoUtils.getFromDto(dto);
            try {
                server.login(user, this);
                return new OkResponse();
            } catch (Error e) {
                connected=false;
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof LogoutRequest) {
            System.out.println("Logout request");
            LogoutRequest logoutRequest = (LogoutRequest)request;
            UserDto dto = logoutRequest.getUser();
            User user= DtoUtils.getFromDto(dto);
            try {
                server.logout(user,this);
                connected=false;
                return new OkResponse();
            } catch (Error e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof GetShowsRequest) {
            System.out.println("Get shows request");
            GetShowsRequest getShowsRequest = (GetShowsRequest)request;
            try {
                Show[] shows = server.getAll();
                ShowDto[] showDtos = DtoUtils.getDto(shows);
                return new GetShowsResponse(showDtos);
            } catch (Error e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        if (request instanceof GetArtistsByDateRequest) {
            System.out.println("Get artists by date request");
            GetArtistsByDateRequest getArtistsByDateRequest = (GetArtistsByDateRequest)request;
            String date = getArtistsByDateRequest.getDate();
            try {
                Artist[] artists = server.getArtists(date);
                ArtistDto[] artistDtos = DtoUtils.getDto(artists);
                return new GetArtistsByDateResponse(artistDtos);
            } catch (Error e) {
                return new ErrorResponse(e.getMessage());
            }
        }

        if (request instanceof BuyTicketRequest) {
            System.out.println("Buy ticket request");
            BuyTicketRequest buyTicketRequest = (BuyTicketRequest) request;
            TicketDto ticketDto = buyTicketRequest.getTicket();
            Ticket ticket = DtoUtils.getFromDto(ticketDto);
            try {
                server.sellTickets(ticket);
                return new OkResponse();
            } catch (InvalidPurchaseException | Error e) {
                return new ErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    @Override
    public void ticketSold(Ticket ticket) throws Error {
        TicketDto dto = DtoUtils.getDto(ticket);
        System.out.println("Ticket sold  "+ ticket);
        try {
            sendResponse(new BuyTicketResponse(dto));
        } catch (IOException e) {
            throw new Error("Sending error: "+e);
        }
    }
}
