import domain.Artist;
import domain.Show;
import domain.Ticket;
import domain.User;
import dtos.*;
import objectprotocol.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesObjectProxy implements IService {
    private String host;
    private int port;
    private IObserver client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private BlockingQueue<Response> responseBlockingQueue;
    private volatile boolean finished;

    public ServicesObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responseBlockingQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void login(User user, IObserver client) throws Error {
        initializeConnection();
        UserDto dto = DtoUtils.getDto(user);
        sendRequest(new LoginRequest(dto));
        Response response = readResponse();
        if (response instanceof OkResponse) {
            this.client = client;
            return;
        }
        if (response instanceof ErrorResponse) {
            ErrorResponse error = (ErrorResponse) response;
            closeConnection();
            throw new Error(error.getMessage());
        }
    }

    @Override
    public void logout(User user, IObserver client) throws Error {
        UserDto dto = DtoUtils.getDto(user);
        sendRequest(new LogoutRequest(dto));
        Response response = readResponse();
        closeConnection();
        if (response instanceof ErrorResponse) {
            ErrorResponse error = (ErrorResponse) response;
            throw new Error(error.getMessage());
        }
    }

    @Override
    public Show[] getAll() throws Error {
        sendRequest(new GetShowsRequest());
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse error = (ErrorResponse) response;
            throw new Error(error.getMessage());
        }
        GetShowsResponse resp = (GetShowsResponse) response;
        ShowDto[] showsDto = resp.getShows();
        return DtoUtils.getFromDto(showsDto);
    }

    @Override
    public Artist[] getArtists(String date) throws Error {
        sendRequest(new GetArtistsByDateRequest(date));
        Response response = readResponse();
        if (response instanceof ErrorResponse) {
            ErrorResponse error = (ErrorResponse) response;
            throw new Error(error.getMessage());
        }
        GetArtistsByDateResponse resp = (GetArtistsByDateResponse) response;
        ArtistDto[] artistDtos = resp.getArtists();
        return DtoUtils.getFromDto(artistDtos);
    }

    @Override
    public void sellTickets(Ticket ticket) throws Error {
        TicketDto dto = DtoUtils.getDto(ticket);
        sendRequest(new BuyTicketRequest(dto));
        Response response = readResponse();
        if (response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new Error(err.getMessage());
        }
    }

    private void sendRequest(Request request) throws Error {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new Error("Error sending object : " + e.getMessage());
        }
    }

    private Response readResponse() {
        Response response = null;
        try{
            response = responseBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void handleUpdate(UpdateResponse update) throws Error {
        if (update instanceof BuyTicketResponse) {
            BuyTicketResponse response = (BuyTicketResponse) update;
            Ticket ticket = DtoUtils.getFromDto(response.getTicket());
            System.out.println("HANDLE UPDATE " + ticket);
            client.ticketSold(ticket);
        }
    }

    private void initializeConnection() {
        try {
            System.out.println("INITIALIZE CONNECTION");
            connection = new Socket(host,port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void closeConnection() {
        finished = true;
        try{
            input.close();
            output.close();
            if (!connection.isClosed()) {
                connection.close();
            }
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while(!finished){
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (response instanceof UpdateResponse) {
                        System.out.println("UPDATEEEEEEEEEEEEEEEEEE");
                        handleUpdate((UpdateResponse)response);
                    } else {
                        try {
                            responseBlockingQueue.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException | Error e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
