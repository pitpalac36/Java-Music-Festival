package festival.persistance;
import festival.model.domain.Artist;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ArtistDbRepository implements IArtistRepository {
    private DbUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(ArtistDbRepository.class.getName());

    public ArtistDbRepository(Properties properties) {
        logger.info("Initializing repository.festival.persistance.ArtistDbRepository with properties : {}", properties);
        dbUtils = new DbUtils(properties);
    }

    @Override
    public void create(Artist artist) throws SQLException {
        logger.trace("entry create");
        try (PreparedStatement statement = dbUtils.getConnection().prepareStatement("INSERT INTO artists (id, name, location, date, availableTicketsNumber) VALUES (?, ?, ?, ?, ?)")) {
            statement.setInt(1, artist.getId());
            statement.setString(2, artist.getName());
            statement.setString(3, artist.getLocation());
            statement.setString(4, artist.getDate());
            statement.setInt(5, artist.getAvailableTicketsNumber());
            int added = statement.executeUpdate();
            if (added == 1) {
                logger.info("added {}", artist);
            }
            else {
                logger.error("failed to add {} ", artist);
            }
            logger.trace("exit add");
        } catch (SQLException e) {
            logger.error(e);
            throw e;
        }
    }

    @Override
    public void update(int id, Artist artist) {
        logger.trace("entry update");
        try (PreparedStatement statement = dbUtils.getConnection().prepareStatement("UPDATE artists SET name=?, location=?, date=?, availableTicketsNumber=? WHERE id=?")) {
            statement.setString(1, artist.getName());
            statement.setString(2, artist.getLocation());
            statement.setString(3, artist.getDate());
            statement.setInt(4, artist.getAvailableTicketsNumber());
            statement.setInt(5, id);
            statement.executeUpdate();
            logger.info("updated artist : {}", artist);
            logger.trace("exit update");
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        logger.trace("entry delete");
        try (PreparedStatement statement = dbUtils.getConnection().prepareStatement("DELETE FROM artists where id = ?")) {
            statement.setInt(1, id);
            int deleted = statement.executeUpdate();
            if (deleted == 1) {
                logger.info("deleted artist with id {}", id);
            }
            else {
                logger.error("failed to delete artist with id {} ", id);
                throw new Exception("No artist with this id was found!");
            }
            logger.trace("exit add");
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public Artist findOne(int id) {
        logger.trace("entry findOne");
        try (PreparedStatement statement = dbUtils.getConnection().prepareStatement("SELECT * FROM artists WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    logger.info(String.format("festival.model.domain.Artist with id %s was found", id));
                    return new Artist(result.getInt("id"), result.getString("name"), result.getString("location"), result.getString("date"), result.getInt("availableTicketsNumber"));
                } else {
                    logger.info(String.format("festival.model.domain.Artist with id %s was not found", id));
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Artist> findAll() {
        logger.trace("entry findAll");
        List<Artist> listResult = new ArrayList<>();
        try (PreparedStatement statement = dbUtils.getConnection().prepareStatement("SELECT * FROM artists")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Artist current = null;
                    current = new Artist(result.getInt("id"), result.getString("name"), result.getString("location"), result.getString("date"), result.getInt("availableTicketsNumber"));
                    listResult.add(current);
                }
            }
            logger.trace("exit findAll");
            return listResult;
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
            return listResult;
        }
    }

    @Override
    public void disconnect() {
        dbUtils.closeConnection();
    }
}
