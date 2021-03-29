package controller;
import domain.Show;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import service.TicketingService;
import validator.InvalidPurchaseException;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {
    @FXML
    public TextField purchaserNameField;

    @FXML
    public Button sellButton;

    @FXML
    public DatePicker datePicker;

    @FXML
    public CheckBox viewCheckbox;

    @FXML
    public Button logOutButton;

    @FXML
    public TableView<Show> showsTableView;

    @FXML
    public TableColumn<Show, Integer> idColumn;

    @FXML
    public TableColumn<Show, String> artistNameColumn;

    @FXML
    public TableColumn<Show, Date> dateColumn;

    @FXML
    public TableColumn<Show, String> locationColumn;

    @FXML
    public TableColumn<Show, Integer> noAvailableTicketsColumn;

    @FXML
    public TableColumn<Show, Integer> noSoldTicketsColumn;

    @FXML
    public Spinner<Integer> noTicketsSpinner;


    private TicketingService service;

    private Stage primaryStage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void initializeModel() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        artistNameColumn.setCellValueFactory(new PropertyValueFactory<>("artistName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        noAvailableTicketsColumn.setCellValueFactory(new PropertyValueFactory<>("availableTicketsNumber"));
        noSoldTicketsColumn.setCellValueFactory(new PropertyValueFactory<>("soldTicketsNumber"));
        noTicketsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100,1));
        populateList();
    }

    private void populateList() {
        showsTableView.getItems().clear();
        showsTableView.getItems().setAll(service.getAll());
        showsTableView.setRowFactory(x -> new TableRow<Show>() {
            @Override
            protected void updateItem(Show item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null) {
                    setStyle("");
                }
                else if (item.getAvailableTicketsNumber() == 0) {
                    setStyle("-fx-background-color: #A53A3B; ");
                }
            }
        });
    }

    public void setService(TicketingService service, Stage stage) {
        this.service = service;
        this.primaryStage = stage;
        initializeModel();
    }

    public void sellTickets() {
        if (purchaserNameField.getText().trim().isEmpty()) {
            showAlert("Please introduce purchaser name!", Alert.AlertType.ERROR);
        } else {
            try {
                if (showsTableView.getSelectionModel().isEmpty()) {
                    showAlert("Please select a show!", Alert.AlertType.WARNING);
                } else {
                    service.buyTickets(showsTableView.getSelectionModel().getSelectedItem().getId(), noTicketsSpinner.getValue(), purchaserNameField.getText());
                    showAlert("The sale was successful!", Alert.AlertType.CONFIRMATION);
                    populateList();
                }
            } catch (InvalidPurchaseException e) {
                showAlert(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void viewArtists() throws IOException {
        if (datePicker.getValue() == null) {
            showAlert("Please select a date first!", Alert.AlertType.WARNING);
            viewCheckbox.fire();
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/artistsView.fxml"));

            Pane layout = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle(datePicker.getValue().toString());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.initOwner(primaryStage);

            Scene scene = new Scene(layout);
            dialog.setScene(scene);

            ModalViewController modalViewController = loader.getController();
            modalViewController.setService(service, dialog, datePicker.getValue().toString());
            dialog.show();

            viewCheckbox.fire();
        }
    }

    public void logout() {
        primaryStage.close();
    }

    private void showAlert(String message, Alert.AlertType type){
        Alert alert = new Alert(type);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
