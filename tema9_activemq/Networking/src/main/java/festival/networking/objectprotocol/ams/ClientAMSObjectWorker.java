package festival.networking.objectprotocol.ams;
import festival.domain.models.Artist;
import festival.domain.models.Show;
import festival.domain.models.Ticket;
import festival.domain.models.User;
import festival.networking.dtos.*;
import festival.networking.objectprotocol.Request;
import festival.networking.objectprotocol.RequestType;
import festival.networking.objectprotocol.Response;
import festival.networking.objectprotocol.ResponseType;
import festival.services.Error;
import festival.services.IServicesAMS;
import festival.validator.InvalidPurchaseException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientAMSObjectWorker implements Runnable {
    private IServicesAMS server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public ClientAMSObjectWorker(IServicesAMS server, Socket connection) {
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
                Response response = handleRequest((Request) request);
                sendResponse(response);
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

    private Response handleRequest(Request request){
        Response response = new Response();
        if (request.type()== RequestType.LOGIN) {
            System.out.println("Login request ...");
            UserDto dto = (UserDto) request.data();
            User user= DtoUtils.getFromDto(dto);
            try {
                server.login(user);
                response.type(ResponseType.OK);
                response.data("Ok");
                return response;
            } catch (Error e) {
                connected=false;
                response.type(ResponseType.ERROR);
                response.data(e.getMessage());
                return response;
            }
        }
        if (request.type()== RequestType.LOGOUT) {
            System.out.println("Logout request");
            UserDto dto = (UserDto) request.data();
            User user= DtoUtils.getFromDto(dto);
            try {
                server.logout(user);
                connected=false;
                response.type(ResponseType.OK);
                response.data("Ok");
                return response;
            } catch (Error e) {
                response.type(ResponseType.ERROR);
                response.data(e.getMessage());
                return response;
            }
        }
        if (request.type()== RequestType.GET_SHOWS) {
            System.out.println("Get shows request");
            try {
                Show[] shows = server.getAll();
                ShowDto[] showDtos = DtoUtils.getDto(shows);
                response.type(ResponseType.GET_SHOWS);
                response.data(showDtos);
                return response;
            } catch (Error e) {
                response.type(ResponseType.ERROR);
                response.data(e.getMessage());
                return response;
            }
        }
        if (request.type()== RequestType.GET_ARTISTS_BY_DATE) {
            System.out.println("Get artists by date request");
            String date = (String)request.data();
            try {
                Artist[] artists = server.getArtists(date);
                ArtistDto[] artistDtos = DtoUtils.getDto(artists);
                response.type(ResponseType.GET_ARTISTS_BY_DATE);
                response.data(artistDtos);
                return response;
            } catch (Error e) {
                response.type(ResponseType.ERROR);
                response.data(e.getMessage());
                return response;
            }
        }

        if (request.type()== RequestType.BUY_TICKET) {
            System.out.println("Buy ticket request");
            Ticket ticketDto = (Ticket) request.data();
            try {
                server.sellTickets(ticketDto);
                response.type(ResponseType.OK);
                return response;
            } catch (Error | InvalidPurchaseException e) {
                response.type(ResponseType.ERROR);
                response.data(e.getMessage());
                return response;
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

}
