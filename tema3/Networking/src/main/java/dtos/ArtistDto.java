package dtos;

import java.io.Serializable;

public class ArtistDto implements Serializable {
    private String name;
    private String location;
    private String date;
    private Integer availableTicketsNumber;

    public ArtistDto(String name, String location, String date, Integer availableTicketsNumber) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.availableTicketsNumber = availableTicketsNumber;
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
}
