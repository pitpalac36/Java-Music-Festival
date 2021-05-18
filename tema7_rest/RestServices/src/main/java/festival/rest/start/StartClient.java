package festival.rest.start;
import festival.model.domain.Artist;
import festival.rest.client.UserClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class StartClient {
    private final static UserClient artistsClient = new UserClient();
    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();
        Artist artist = new Artist(1000, "Alternosfera", "Cluj-Napoca, olteniei 3/10", "11-11-2022", 100);
        try{
            //create
            show(() -> System.out.println("Artistul adaugat: " + artistsClient.create(artist)));

            //update
            artist.setAvailableTicketsNumber(99);
            show(() -> artistsClient.update(artist, artist.getId()));

            //findOne
            show(() -> System.out.println("Artistul modificat: " + artistsClient.getById(artist.getId())));

            //findAll before delete
            show(() -> {
                Artist[] artisti = artistsClient.getAll();
                System.out.println("Artistii existenti inainte de delete: ");
                for (Artist a : artisti) {
                    System.out.println(a);
                }
            });

            //delete
            show(() -> artistsClient.delete(artist.getId()));

            //findAll after delete
            show(() -> {
                Artist[] artisti = artistsClient.getAll();
                System.out.println("Artistii existenti dupa delete: ");
                for (Artist a : artisti) {
                    System.out.println(a);
                }
            });
        }
        catch (RestClientException ex){
            System.out.println("Exception: " + ex.getMessage());
        }

    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (Exception e) {
            System.out.println("Service exception: "+ e);
        }
    }
}
