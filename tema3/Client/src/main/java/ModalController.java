import domain.Artist;
import domain.Show;
import domain.Ticket;
import javafx.application.Platform;
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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModalController implements Initializable, IObserver {
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

    @Override
    public void ticketSold(Ticket ticket) throws Error {
        Platform.runLater(() -> {
            try {
                artists.setAll(server.getArtists(date));
            } catch (Error error) {
                error.printStackTrace();
            }
            artistsTableView.getItems().clear();
            List<Artist> list = artists;
            list.forEach(x -> {
                artistsTableView.getItems().add(x);
                System.out.println(x + " ADDED");
            });
        });
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

    private void setData() throws Error {
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
