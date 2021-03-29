package controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import repository.IShowRepository;
import repository.ITicketRepository;
import repository.ShowDbRepository;
import repository.TicketDbRepository;
import service.TicketingService;
import service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    public TextField usernameTextField;

    @FXML
    public PasswordField passwordTextField;

    @FXML
    public Button loginButton;

    private UserService service;

    private Properties properties;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void login(Event mouseEvent) throws IOException {
        StringBuilder errors = new StringBuilder();
        if (usernameTextField.getText().trim().isEmpty()) {
            errors.append("Username is empty!");
        }
        if (passwordTextField.getText().trim().isEmpty()) {
            errors.append("Password is empty!");
        }
        if (!errors.toString().isEmpty()) {
            showAlert(errors.toString(), Alert.AlertType.ERROR);
            usernameTextField.clear();
            passwordTextField.clear();
        }
        else {
            if (service.login(usernameTextField.getText(), passwordTextField.getText())) {

                ITicketRepository ticketRepository = new TicketDbRepository(properties);
                IShowRepository showRepository = new ShowDbRepository(properties);
                TicketingService ticketingService = new TicketingService(showRepository, ticketRepository);

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/mainView.fxml"));
                Pane layout = loader.load();

                Stage stage = (Stage)((Node) mouseEvent.getSource()).getScene().getWindow();
                stage.setTitle(usernameTextField.getText() + "'s window");

                MainViewController mainViewController = loader.getController();
                mainViewController.setService(ticketingService, stage);
                stage.setScene(new Scene(layout));
                stage.show();
            } else {
                showAlert("Invalid credentials!", Alert.AlertType.ERROR);
                usernameTextField.clear();
                passwordTextField.clear();
            }
        }
    }

    private void showAlert(String message, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public void setService(UserService service, Properties properties) {
        this.service = service;
        this.properties = properties;
    }
}
