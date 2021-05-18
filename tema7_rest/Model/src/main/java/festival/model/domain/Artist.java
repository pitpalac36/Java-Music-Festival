package festival.model.domain;
import java.io.Serializable;
import java.util.Objects;

public class Artist implements Serializable {
    private int id;
    private String name;
    private String location;
    private String date;
    private Integer availableTicketsNumber;

    public Artist() {}

    public Artist(int id, String name, String location, String date, int availableTicketsNumber) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.availableTicketsNumber = availableTicketsNumber;
    }

    public Artist(String name, String location, String date, int availableTicketsNumber) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.availableTicketsNumber = availableTicketsNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAvailableTicketsNumber() {
        return availableTicketsNumber;
    }

    public void setAvailableTicketsNumber(Integer availableTicketsNumber) {
        this.availableTicketsNumber = availableTicketsNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist showDTO = (Artist) o;
        return availableTicketsNumber.equals(showDTO.availableTicketsNumber) &&
                Objects.equals(name, showDTO.name) &&
                Objects.equals(location, showDTO.location) &&
                Objects.equals(date, showDTO.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, date, availableTicketsNumber);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", availableTicketsNumber=" + availableTicketsNumber +
                '}';
    }
}