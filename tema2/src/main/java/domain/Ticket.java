package domain;

import java.util.Objects;

public class Ticket {
    private int showId;
    private String purchaserName;
    private int number;

    public Ticket(int showId, String purchaserName, int number) {
        this.showId = showId;
        this.purchaserName = purchaserName;
        this.number = number;
    }

    public int getShowId() {
        return showId;
    }

    public void setShowId(int showId) {
        this.showId = showId;
    }

    public String getPurchaserName() {
        return purchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        this.purchaserName = purchaserName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "showId=" + showId +
                ", purchaserName='" + purchaserName + '\'' +
                ", number=" + number +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return showId == ticket.showId &&
                number == ticket.number &&
                Objects.equals(purchaserName, ticket.purchaserName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(showId, purchaserName, number);
    }
}
