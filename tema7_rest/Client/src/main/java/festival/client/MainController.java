package festival.client;

import festival.model.domain.Show;
import festival.model.domain.Ticket;
import festival.model.domain.User;
import festival.services.Error;
import festival.services.IObserver;
import festival.services.IService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import festival.model.validator.InvalidPurchaseException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.ResourceBundle;

public class MainController extends UnicastRemoteObject implements Initializable, IObserver, Serializable {
    @FXML
    public TableView<Show> showsTableView;

    @FXML
    public TableColumn<Show, String> idColumn;

    @FXML
    public TableColumn<Show, String> artistNameColumn;

    @FXML
    public TableColumn<Show, String> dateColumn;

    @FXML
    public TableColumn<Show, String> locationColumn;

    @FXML
    public TableColumn<Show, Integer> noAvailableTicketsColumn;

    @FXML
    public TableColumn<Show, Integer> noSoldTicketsColumn;

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
    public Spinner<Integer> noTicketsSpinner;

    private Stage stage;

    private IService server;
    private User user;
    private ObservableList<Show> shows = FXCollections.observableArrayList();

    public MainController() throws RemoteException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @Override
    public void ticketSold(Ticket ticket) {
        Platform.runLater(() -> {
            try {
                shows.setAll(server.getAll());
            } catch (Error error) {
                error.printStackTrace();
            }
            showsTableView.getItems().clear();
            List<Show> list = shows;
            list.forEach(x -> {
                showsTableView.getItems().add(x);
                System.out.println(x + " ADDED");
            });
        });
    }

    public void logout() {
        try {
            server.logout(user, this);
            stage.close();
            System.exit(0);
        } catch (Error error) {
            System.out.println("Logout error : " + error.getMessage());
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setServer(IService server) {
        this.server = server;
    }

    private void setData() throws Error {
        shows.setAll(server.getAll());
        System.out.println("SHOWS : " + shows.size());
    }

    public void setStage(Stage stage) throws Error {
        this.stage = stage;
        setData();
        populateTable();
    }

    private void populateTable() {
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
        showsTableView.getItems().addAll(shows);
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

    public void sellTickets() {
        if (purchaserNameField.getText().trim().isEmpty()) {
            Utils.showAlert("Please introduce purchaser name!", Alert.AlertType.ERROR);
        } else {
            try {
                if (showsTableView.getSelectionModel().isEmpty()) {
                    Utils.showAlert("Please select a show!", Alert.AlertType.WARNING);
                } else {
                    Ticket ticket = new Ticket(showsTableView.getSelectionModel().getSelectedItem().getId(), purchaserNameField.getText(), noTicketsSpinner.getValue());
                    server.sellTickets(ticket);
                    Utils.showAlert("The sale was successful!", Alert.AlertType.CONFIRMATION);
                }
            } catch (InvalidPurchaseException | Error e) {
                Utils.showAlert(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    public void viewArtists() throws IOException, Error {
        if (datePicker.getValue() == null) {
            Utils.showAlert("Please select a date first!", Alert.AlertType.WARNING);
            viewCheckbox.fire();
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/artistsView.fxml"));

            Pane layout = loader.load();

            Stage dialog = new Stage();
            dialog.setTitle(datePicker.getValue().toString());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.initOwner(stage);

            Scene scene = new Scene(layout);
            dialog.setScene(scene);

            ModalController modalController = loader.getController();
            modalController.setServer(server, dialog, datePicker.getValue().toString());
            dialog.show();

            viewCheckbox.fire();
        }
    }
}
