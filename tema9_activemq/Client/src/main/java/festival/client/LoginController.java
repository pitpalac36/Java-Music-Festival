package festival.client;

import festival.domain.models.User;
import festival.networking.objectprotocol.ams.ServicesAMSObjectProxy;
import festival.services.Error;
import festival.services.IServicesAMS;
import festival.services.NotificationReceiver;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController{
    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordTextField;

    @FXML
    public Button loginButton;

    public IServicesAMS server;

    public Stage stage;

    public MainController mainController;

    private NotificationReceiver receiver;

    public LoginController() {}

    public LoginController(ServicesAMSObjectProxy servicesAMSObjectProxy, NotificationReceiver receiver) {
        server = servicesAMSObjectProxy;
        this.receiver = receiver;
    }

    public void setServer(IServicesAMS s, Stage stage) {
        server = s;
        this.stage = stage;
    }

    public void setController(MainController c) {
        mainController = c;
    }

    public void login(Event mouseEvent) {
        StringBuilder errors = new StringBuilder();
        if (usernameTextField.getText().trim().isEmpty()) {
            errors.append("Username is empty!");
        }
        if (passwordTextField.getText().trim().isEmpty()) {
            errors.append("Password is empty!");
        }
        if (!errors.toString().isEmpty()) {
            Utils.showAlert(errors.toString(), Alert.AlertType.ERROR);
            usernameTextField.clear();
            passwordTextField.clear();
        } else {
            User user = new User(usernameTextField.getText(), passwordTextField.getText());
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("mainView.fxml"));
                Parent layout = loader.load();

                Stage mainStage = new Stage();
                mainStage.setTitle(usernameTextField.getText() + "'s window");

                MainController mainController = loader.getController();
                mainController.setServer(server);
                mainController.setNotificationReceiver(receiver);

                server.login(user);
                receiver.start(mainController);

                Scene scene = new Scene(layout);
                mainStage.setScene(scene);

                mainStage.setOnCloseRequest(event -> {
                    mainController.logout();
                    System.exit(0);
                });
                stage.hide();

                mainStage.show();
                mainController.setUser(user);
                mainController.setStage(mainStage);
                ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
            } catch (Error error) {
                Utils.showAlert("Invalid credentials!", Alert.AlertType.ERROR);
                usernameTextField.clear();
                passwordTextField.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setNotificationReceiver(NotificationReceiverImpl receiver) {
        this.receiver = receiver;
    }
}