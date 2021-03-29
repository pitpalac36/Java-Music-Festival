import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.UserDbRepository;
import service.UserService;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Properties properties = new Properties();
        try (Reader reader = new FileReader("bd.config")) {
            properties.load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        UserService service = new UserService(new UserDbRepository(properties));

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/login.fxml"));
        Object layout = loader.load();

        stage = new Stage();
        stage.setTitle("MusicFest Management App");
        LoginController loginController = loader.getController();
        loginController.setService(service, properties);
        stage.setScene(new Scene((Parent) layout));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
