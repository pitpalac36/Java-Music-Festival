package repository;
import domain.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class UserDbRepository implements IUserRepository {
    private DbUtils dbUtils;
    private static final Logger logger = LogManager.getLogger(UserDbRepository.class.getName());

    public UserDbRepository(Properties properties) {
        logger.info("Initializing repository.UserDbRepository with properties : {}", properties);
        dbUtils = new DbUtils(properties);
    }

    @Override
    public boolean findOne(String username, String password) {
        logger.trace("entry findOne");
        try (PreparedStatement statement = dbUtils.getConnection().prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    logger.info(String.format("User with username : %s and password : %s was found!", username, password));
                    return true;
                } else {
                    logger.info(String.format("User with username : %s and password : %s not found!", username, password));
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<User> findAll() {
        logger.trace("entry findAll");
        try (PreparedStatement statement = dbUtils.getConnection().prepareStatement("SELECT * FROM users")) {
            List<User> listResult = new ArrayList<>();
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    User current = new User(result.getString("username"), result.getString("password"));
                    listResult.add(current);
                }
            }
            logger.trace("exit findAll");
            return listResult;
        } catch (SQLException e) {
            logger.error(e);
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect() {
        dbUtils.closeConnection();
    }
}
