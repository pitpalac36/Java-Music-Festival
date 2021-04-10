package domain;
import java.io.Serializable;
import java.util.Objects;

public class Show implements Serializable {
    private int id;
    private String artistName;
    private String date;
    private String location;
    private int availableTicketsNumber;
    private int soldTicketsNumber;

    public Show(int id, String artistName, String date, String location, int availableTicketsNumber, int soldTicketsNumber) {
        this.id = id;
        this.artistName = artistName;
        this.date = date;
        this.location = location;
        this.availableTicketsNumber = availableTicketsNumber;
        this.soldTicketsNumber = soldTicketsNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getAvailableTicketsNumber() {
        return availableTicketsNumber;
    }

    public void setAvailableTicketsNumber(int availableTicketsNumber) {
        this.availableTicketsNumber = availableTicketsNumber;
    }

    public int getSoldTicketsNumber() {
        return soldTicketsNumber;
    }

    public void setSoldTicketsNumber(int soldTicketsNumber) {
        this.soldTicketsNumber = soldTicketsNumber;
    }

    @Override
    public String toString() {
        return "domain.Show{" +
                "id=" + id +
                ", artistName='" + artistName + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", availableTicketsNumber=" + availableTicketsNumber +
                ", soldTicketsNumber=" + soldTicketsNumber +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Show show = (Show) o;
        return id == show.id &&
                availableTicketsNumber == show.availableTicketsNumber &&
                soldTicketsNumber == show.soldTicketsNumber &&
                Objects.equals(artistName, show.artistName) &&
                Objects.equals(date, show.date) &&
                Objects.equals(location, show.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, artistName, date, location, availableTicketsNumber, soldTicketsNumber);
    }
}