package festival.client;

import festival.model.domain.Artist;
import festival.services.Error;
import festival.services.IService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.Serializable;
import java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;

public class ModalController extends UnicastRemoteObject implements Initializable, Serializable {
    @FXML
    public TableView<Artist> artistsTableView;

    @FXML
    public TableColumn<Artist, String> locationColumn;

    @FXML
    public Button okButton;

    @FXML
    public TableColumn<Artist, String> nameColumn;

    @FXML
    public TableColumn<Artist, String> dateColumn;

    @FXML
    public TableColumn<Artist,Integer> availableTicketsNumberColumn;

    private IService server;
    private Stage stage;
    private String date;
    private ObservableList<Artist> artists = FXCollections.observableArrayList();

    public ModalController() throws RemoteException {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void initializeModel() throws Error {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        availableTicketsNumberColumn.setCellValueFactory(new PropertyValueFactory<>("availableTicketsNumber"));
        setData();
        populateList();
    }

    private void setData() throws Error, Error {
        artists.setAll(server.getArtists(date));
        System.out.println("ARTISTS : " + artists.size());
    }

    private void populateList() throws Error {
        artistsTableView.getItems().clear();
        artistsTableView.getItems().setAll(server.getArtists(date));
        artistsTableView.setRowFactory(x -> new TableRow<Artist>() {
            @Override
            protected void updateItem(Artist item, boolean empty) {
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

    public void setServer(IService server, Stage dialog, String date) throws Error {
        this.server = server;
        this.stage = dialog;
        this.date = date;
        initializeModel();
    }

    public void closeModal(MouseEvent mouseEvent) {
        stage.close();
    }
}
