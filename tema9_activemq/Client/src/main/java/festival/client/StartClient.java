package festival.client;
import festival.networking.objectprotocol.ams.ServicesAMSObjectProxy;
import festival.services.IServicesAMS;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import javax.jms.Session;
import java.io.IOException;
import java.util.Properties;


public class StartClient extends Application {

    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartClient.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        try {
            ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:spring-client.xml");
            IServicesAMS server = (ServicesAMSObjectProxy) factory.getBean("service");

            JmsTemplate jms = (JmsTemplate) factory.getBean("jmsTemplate");
            jms.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
            jms.setSessionTransacted(true);

            FXMLLoader loginLoader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
            Parent root = loginLoader.load();
            LoginController loginController = loginLoader.<LoginController>getController();
            loginController.setServer(server, primaryStage);
            NotificationReceiverImpl receiver = (NotificationReceiverImpl)factory.getBean("notificationReceiver");
            loginController.setNotificationReceiver(receiver);

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Login");
            primaryStage.show();
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
        }
    }
}