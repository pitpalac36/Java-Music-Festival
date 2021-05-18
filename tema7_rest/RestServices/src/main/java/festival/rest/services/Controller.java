package festival.rest.services;
import festival.model.domain.Artist;
import festival.persistance.ArtistDbRepository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/artists")
public class Controller {

    private ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-rest.xml");
    private ArtistDbRepository artistRepo = (ArtistDbRepository) factory.getBean("artistRepo");

    @GetMapping
    public Artist[] getAll() {
        System.out.println("INSIDE CONTROLLER GET ALL");
        List<Artist> all = artistRepo.findAll();
        System.out.println(all.size());
        return all.toArray(new Artist[all.size()]);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable int id) {
        System.out.println("INSIDE CONTROLLER GET BY ID");
        Artist artist = artistRepo.findOne(id);
        if (artist == null)
            return new ResponseEntity<String>("Artist not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Artist>(artist, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> create(@RequestBody Artist artist){
        try {
            artistRepo.create(artist);
        } catch (Exception e) {
            return new ResponseEntity<String>("Invalid object!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Artist>(artist, HttpStatus.CREATED);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int id){
        System.out.println("INSIDE CONTROLLER DELETE");
        try {
            artistRepo.delete(id);
            return new ResponseEntity<Artist>(HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Artist artist, @PathVariable int id){
        try {
            artistRepo.update(id, artist);
        } catch (Exception e) {
            return new ResponseEntity<String>("Invalid values!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Artist>(artist, HttpStatus.NO_CONTENT);
    }
}
