package repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class DbUtils {

    private Properties jdbcProps;

    private static final Logger logger= LogManager.getLogger(DbUtils.class.getName());

    public DbUtils(Properties props){
        jdbcProps=props;
    }

    private  Connection instance=null;

    private Connection getNewConnection() throws ClassNotFoundException {
        logger.traceEntry();
        String url=jdbcProps.getProperty("jdbc.url");
        String user=jdbcProps.getProperty("jdbc.user");
        String pass=jdbcProps.getProperty("jdbc.pass");
        logger.info("trying to connect to database ... {}",url);
        logger.info("user: {}",user);
        logger.info("pass: {}", pass);
        Connection con=null;
        try {
            Class.forName("org.sqlite.JDBC");
            if (user!=null && pass!=null)
                con= DriverManager.getConnection(url,user,pass);
            else
                con=DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error(e);
            logger.error("Error getting connection {}" + e);
        }
        return con;
    }

    public Connection getConnection(){
        logger.traceEntry();
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException | ClassNotFoundException e) {
            logger.error(e);
            logger.error("Error DB " + e);
        }
        logger.traceExit(instance);
        return instance;
    }

    public void closeConnection() {
        logger.trace("entry closeConnection");
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                logger.info("connection {} successfully closed!", instance);
            }
            else {
                logger.info("connection {} was already closed or was null", instance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }
}