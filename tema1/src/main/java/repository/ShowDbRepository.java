package repository;
import domain.Show;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ShowDbRepository implements IShowRepository {
    private DbUtils dbUtils;
    private Connection connection;
    private static final Logger logger = LogManager.getLogger(ShowDbRepository.class.getName());

    public ShowDbRepository(Properties properties) {
        logger.info("Initializing repository.ShowDbRepository with properties : {}", properties);
        dbUtils = new DbUtils(properties);
        connection = dbUtils.getConnection();
    }

    @Override
    public Show findOne(int id) {
        logger.trace("entry findOne");
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM shows WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    logger.info(String.format("Show with id %s was found", id));
                    try {
                        return new Show(result.getInt("id"), result.getString("artistName"), new SimpleDateFormat("yyyy-MM-dd").parse(result.getString("dateOfEvent")), result.getString("location"), result.getInt("availableTicketsNo"), result.getInt("soldTicketsNo"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.info(String.format("Show with id %s was not found", id));
                    return null;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Show> findArtists(Date date) {
        logger.trace("entry findArtists");
        List<Show> listResult = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM shows WHERE dateOfEvent=?")) {
            logger.info(new SimpleDateFormat("yyyy-MM-dd").format(date));
            statement.setString(1, new SimpleDateFormat("yyyy-MM-dd").format(date));
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Show current = null;
                    try {
                        current = new Show(result.getInt("id"), result.getString("artistName"), new SimpleDateFormat("yyyy-MM-dd").parse(result.getString("dateOfEvent")), result.getString("location"), result.getInt("availableTicketsNo"), result.getInt("soldTicketsNo"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    listResult.add(current);
                }
            }
            logger.trace("exit findArtists");
            return listResult;
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
            return listResult;
        }
    }

    @Override
    public void update(Show show) {
        logger.trace("entry update");
        try (PreparedStatement statement = connection.prepareStatement("UPDATE shows SET artistName=?, dateOfEvent=?, location=?, availableTicketsNo=?, soldTicketsNo=? WHERE id=?")) {
            statement.setString(1, show.getArtistName());
            statement.setString(2, new SimpleDateFormat("yyyy-MM-dd").format(show.getDate()));
            statement.setString(3, show.getLocation());
            statement.setInt(4, show.getAvailableTicketsNumber());
            statement.setInt(5, show.getSoldTicketsNumber());
            statement.setInt(6, show.getId());
            statement.executeUpdate();
            logger.info("updated show : {}", show);
            logger.trace("exit update");
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public List<Show> findAll() {
        logger.trace("entry findAll");
        List<Show> listResult = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM shows")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Show current = null;
                    try {
                        current = new Show(result.getInt("id"), result.getString("artistName"), new SimpleDateFormat("yyyy-MM-dd").parse(result.getString("dateOfEvent")), result.getString("location"), result.getInt("availableTicketsNo"), result.getInt("soldTicketsNo"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

    public void disconnect() {
        dbUtils.closeConnection();
    }
}
