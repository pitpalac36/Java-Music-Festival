package festival.persistance;

import festival.model.domain.Ticket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TicketDbRepository implements ITicketRepository {
    private DbUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(TicketDbRepository.class.getName());

    public TicketDbRepository(Properties properties) {
        logger.info("Initializing repository.festival.persistance.TicketDbRepository with properties : {}", properties);
        dbUtils = new DbUtils(properties);
    }

    @Override
    public void save(Ticket ticket) {
        logger.trace("entry save");
        try (PreparedStatement statement = dbUtils.getConnection().prepareStatement("INSERT INTO tickets (showId, purchaserName, number) VALUES (?, ?, ?)")) {
            statement.setInt(1, ticket.getShowId());
            statement.setString(2, ticket.getPurchaserName());
            statement.setInt(3, ticket.getNumber());
            int added = statement.executeUpdate();
            if (added == 1) {
                logger.info("added {}", ticket);
            }
            else {
                logger.error("failed to add {} ", ticket);
            }
            logger.trace("exit add");
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

    @Override
    public List<Ticket> findAll() {
        logger.trace("entry findAll");
        List<Ticket> listResult = new ArrayList<>();
        try (PreparedStatement statement = dbUtils.getConnection().prepareStatement("SELECT * FROM tickets")) {
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Ticket current = new Ticket(result.getInt("showId"), result.getString("purchaserName"), result.getInt("number"));
                    listResult.add(current);
                }
            }
            logger.trace("exit findAll");
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
        }
        return listResult;
    }

    public void disconnect() {
        dbUtils.closeConnection();
    }
}