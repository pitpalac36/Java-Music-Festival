package festival.networking.objectprotocol.ams;
import festival.domain.models.Artist;
import festival.domain.models.Show;
import festival.domain.models.Ticket;
import festival.domain.models.User;
import festival.networking.dtos.ArtistDto;
import festival.networking.dtos.DtoUtils;
import festival.networking.dtos.ShowDto;
import festival.networking.dtos.UserDto;
import festival.networking.objectprotocol.Request;
import festival.networking.objectprotocol.RequestType;
import festival.networking.objectprotocol.Response;
import festival.networking.objectprotocol.ResponseType;
import festival.services.IServicesAMS;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesAMSObjectProxy implements IServicesAMS {
    private String host;
    private int port;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private BlockingQueue<Response> responseBlockingQueue;
    private volatile boolean finished;

    public ServicesAMSObjectProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responseBlockingQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void login(User user) throws java.lang.Error {
        initializeConnection();
        UserDto dto = DtoUtils.getDto(user);
        Request req = new Request.Builder().type(RequestType.LOGIN).data(dto).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            closeConnection();
            throw new java.lang.Error(err);
        }
    }

    @Override
    public void logout(User user) throws java.lang.Error {
        UserDto dto = DtoUtils.getDto(user);
        Request req=new Request.Builder().type(RequestType.LOGOUT).data(dto).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err=response.data().toString();
            throw new java.lang.Error(err);
        }
    }

    @Override
    public Show[] getAll() throws java.lang.Error {
        Request req=new Request.Builder().type(RequestType.GET_SHOWS).data("").build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new java.lang.Error(err);
        }
        ShowDto[] showsDto = (ShowDto[]) response.data();
        return DtoUtils.getFromDto(showsDto);
    }

    @Override
    public Artist[] getArtists(String date) throws java.lang.Error {
        Request req=new Request.Builder().type(RequestType.GET_ARTISTS_BY_DATE).data(date).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new java.lang.Error(err);
        }
        ArtistDto[] artistsDto = (ArtistDto[]) response.data();
        return DtoUtils.getFromDto(artistsDto);
    }

    @Override
    public void sellTickets(Ticket ticket) throws java.lang.Error {
        Request req=new Request.Builder().type(RequestType.BUY_TICKET).data(ticket).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            throw new java.lang.Error(err);
        }
    }

    private void sendRequest(Request request) throws java.lang.Error {
        try {
            initializeConnection();
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new java.lang.Error("festival.services.Error sending object : " + e.getMessage());
        }
    }

    private Response readResponse() {
        Response response=null;
        try{
            /*synchronized (responses){
                responses.wait();
            }
            response = responses.remove(0);    */
            response = responseBlockingQueue.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isUpdate(Response response){
        return response.type()== ResponseType.TICKET_SOLD;
    }

    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    Object response = input.readObject();
                    System.out.println("response received "+response);
                    if (isUpdate((Response)response)){}
                    else{
                        try {
                            responseBlockingQueue.put((Response)response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException|ClassNotFoundException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }
}
