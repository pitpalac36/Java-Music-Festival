package festival.networking.dtos;

import java.io.Serializable;

public class TicketDto implements Serializable {
    private int showId;
    private String purchaserName;
    private int number;

    public TicketDto(int showId, String purchaserName, int number) {
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
}
