package festival.persistance;

import festival.model.domain.Show;
import festival.model.domain.Artist;

import java.util.List;

public interface IShowRepository extends IRepository<Show> {
    Show findOne(int id);
    List<Artist> findArtists(String date);
    void update(Show show);
}