import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Properties;


public class StartClient extends Application {
    private static int defaultChatPort = 55555;
    private static String defaultServer = "127.0.0.1";

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
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IService server = new ServicesObjectProxy(serverIP, serverPort);

        FXMLLoader loginLoader = new FXMLLoader(getClass().getClassLoader().getResource("login.fxml"));
        Parent root=loginLoader.load();

        LoginController loginController = loginLoader.<LoginController>getController();
        loginController.setServer(server);

        FXMLLoader mainLoader = new FXMLLoader(getClass().getClassLoader().getResource("mainView.fxml"));
        Parent croot = mainLoader.load();

        MainController mainController = mainLoader.getController();
        mainController.setServer(server);

        loginController.setController(mainController);
        loginController.setParent(croot);

        primaryStage.setTitle("MPP chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}