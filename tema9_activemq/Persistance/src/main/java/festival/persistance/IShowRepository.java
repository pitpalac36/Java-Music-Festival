package festival.persistance;

import festival.domain.models.Show;
import festival.domain.models.Artist;

import java.util.List;

public interface IShowRepository extends IRepository<Show> {
    Show findOne(int id);
    List<Artist> findArtists(String date);
    void update(Show show);
}