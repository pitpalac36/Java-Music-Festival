package controller;
import domain.ShowDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.TicketingService;
import java.net.URL;
import java.util.ResourceBundle;

public class ModalViewController implements Initializable {

    @FXML
    public TableView<ShowDTO> artistsTableView;

    @FXML
    public TableColumn<ShowDTO, String> locationColumn;

    @FXML
    public Button okButton;

    @FXML
    public TableColumn<ShowDTO, String> nameColumn;

    @FXML
    public TableColumn<ShowDTO, String> dateColumn;

    @FXML
    public TableColumn<ShowDTO,Integer> availableTicketsNumberColumn;

    private TicketingService service;
    private Stage stage;
    private String date;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    public void initializeModel() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        availableTicketsNumberColumn.setCellValueFactory(new PropertyValueFactory<>("availableTicketsNumber"));
        populateList();
    }

    private void populateList() {
        artistsTableView.getItems().clear();
        System.out.println(service.getArtists(date));
        artistsTableView.getItems().setAll(service.getArtists(date));
    }

    public void setService(TicketingService service, Stage dialog, String value) {
        this.service = service;
        this.stage = dialog;
        this.date = value;
        initializeModel();
    }

    public void closeModal() {
        stage.close();
    }
}
