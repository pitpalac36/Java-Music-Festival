import domain.Show;
import domain.Ticket;
import domain.User;
import org.junit.Assert;
import org.junit.Test;
import repository.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class TestRepository {
    private static Properties properties = new Properties();

    public void loadProps() {
        try (Reader reader = new FileReader("bd.config")) {
            properties.load(reader);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void testUserRepository() {
        loadProps();
        IUserRepository repo = new UserDbRepository(properties);
        Assert.assertTrue(repo.findOne("test1", "test1"));
        Assert.assertFalse(repo.findOne("asdfcv", "sdf"));
        int i = 1;
        for (User user : repo.findAll()) {
            Assert.assertTrue(user.getUsername().contains("test" + i));
            Assert.assertTrue(user.getPassword().contains("test" + i));
            Assert.assertEquals(user.getUsername(), user.getPassword());
            i++;
        }
        repo.disconnect();
    }

    @Test
    public void testShowRepository() throws ParseException {
        loadProps();
        IShowRepository repo = new ShowDbRepository(properties);
        Show show = repo.findOne(1);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse( "2021-03-15" );
        Assert.assertEquals("descend into despair", show.getArtistName());
        Assert.assertEquals(date, show.getDate());
        Assert.assertEquals("Cluj-Napoca, str. Grivitei, 2/30", show.getLocation());
        Assert.assertEquals(12, show.getAvailableTicketsNumber());
        Assert.assertEquals(88, show.getSoldTicketsNumber());
        System.out.println(repo.findArtists(format.parse( "2021-03-15" )).get(0));
        Assert.assertEquals(show, repo.findArtists(format.parse( "2021-03-15" )).get(0));
        List<Show> shows = repo.findAll();
        Assert.assertTrue(shows.contains(show));
        String random = UUID.randomUUID().toString();
        repo.update(new Show(4, "artist" + random, Date.from(Instant.now()), "location" + random, 100, 100));
        Show updated = repo.findOne(4);
        Assert.assertEquals("artist" + random, updated.getArtistName());
        Assert.assertEquals("location" + random, updated.getLocation());
        repo.disconnect();
    }

    @Test
    public void testTicketRepository() {
        loadProps();
        ITicketRepository repo = new TicketDbRepository(properties);
        List<Ticket> all = repo.findAll();
        Ticket ticket = new Ticket(1, "test", 2);
        repo.save(ticket);
        List<Ticket> allPlusOne = repo.findAll();
        Assert.assertEquals(all.size() + 1, allPlusOne.size());
        System.out.println(allPlusOne);
        Assert.assertTrue(allPlusOne.contains(ticket));
        repo.disconnect();
    }

}
