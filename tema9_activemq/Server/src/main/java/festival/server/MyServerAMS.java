package festival.server;
import festival.networking.objectprotocol.ams.ObjectAMSConcurrentServer;
import festival.networking.utils.AbstractServer;
import festival.networking.utils.ServerException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MyServerAMS {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-server.xml");
        AbstractServer server = context.getBean("tcpServer", ObjectAMSConcurrentServer.class);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("festival.services.Error starting the server" + e.getMessage());
        }
    }
}
