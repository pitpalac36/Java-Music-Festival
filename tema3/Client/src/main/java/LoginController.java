import domain.User;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordTextField;

    @FXML
    public Button loginButton;

    public IService server;

    public Parent parent;

    public MainController mainController;

    public void setServer(IService s) {
        server = s;
    }

    public void setParent(Parent p) {
        parent = p;
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
                server.login(user, mainController);

                Stage stage = new Stage();
                stage.setTitle(usernameTextField.getText() + "'s window");
                stage.setScene(new Scene(parent));

                stage.setOnCloseRequest(event -> {
                    mainController.logout();
                    System.exit(0);
                });
                stage.show();
                mainController.setUser(user);
                mainController.setStage(stage);
                ((Node) (mouseEvent.getSource())).getScene().getWindow().hide();
            } catch (Error error) {
                Utils.showAlert("Invalid credentials!", Alert.AlertType.ERROR);
                usernameTextField.clear();
                passwordTextField.clear();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}