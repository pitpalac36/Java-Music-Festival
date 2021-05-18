package festival.rest.client;
import festival.model.domain.Artist;
import festival.rest.services.ServiceException;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.Callable;

public class UserClient {
    public static final String URL="http://localhost:8080/artists";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) { // server down, resource exception
            throw new ServiceException(e);
        }
    }

    public Artist[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Artist[].class));
    }

    public Artist getById(int id){
        return execute(() -> restTemplate.getForObject(String.format("%s/%d", URL, id), Artist.class));
    }

    public Artist create(Artist artist) {
        return execute(() -> restTemplate.postForObject(URL, artist , Artist.class));
    }

    public void update(Artist artist, int id) {
        execute(() -> {
            restTemplate.put(String.format("%s/%d", URL, id), artist);
            return null;
        });
    }

    public void delete(int id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%d", URL, id));
            return null;
        });
    }

}
