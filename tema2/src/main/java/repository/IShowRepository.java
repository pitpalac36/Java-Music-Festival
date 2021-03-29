package repository;
import domain.Show;
import domain.ShowDTO;
import java.util.List;

public interface IShowRepository extends IRepository<Show> {
    Show findOne(int id);
    List<ShowDTO> findArtists(String date);
    void update(Show show);
}
