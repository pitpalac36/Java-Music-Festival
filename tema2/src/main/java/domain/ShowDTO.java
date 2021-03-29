package domain;
import java.util.Objects;

public class ShowDTO {
    private String name;
    private String location;
    private String date;
    private Integer availableTicketsNumber;

    public ShowDTO(String name, String location, String date, int availableTicketsNumber) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.availableTicketsNumber = availableTicketsNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShowDTO showDTO = (ShowDTO) o;
        return availableTicketsNumber == showDTO.availableTicketsNumber &&
                Objects.equals(name, showDTO.name) &&
                Objects.equals(location, showDTO.location) &&
                Objects.equals(date, showDTO.date);
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public Integer getAvailableTicketsNumber() {
        return availableTicketsNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, location, date, availableTicketsNumber);
    }

    @Override
    public String toString() {
        return "ShowDTO{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", availableTicketsNumber=" + availableTicketsNumber +
                '}';
    }
}
