package festival.persistance;

import festival.model.domain.Artist;

import java.sql.SQLException;

public interface IArtistRepository extends IRepository<Artist> {
    void create(Artist artist) throws SQLException;
    void update(int id, Artist artist);
    void delete(int id) throws Exception;
    Artist findOne(int id);
}
