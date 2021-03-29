package repository;
import domain.Show;
import java.util.Date;
import java.util.List;

public interface IShowRepository extends IRepository<Show> {
    Show findOne(int id);
    List<Show> findArtists(Date date);
    void update(Show show);
}
