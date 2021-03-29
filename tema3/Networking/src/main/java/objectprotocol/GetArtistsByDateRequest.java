package objectprotocol;

public class GetArtistsByDateRequest implements Request {
    private String date;

    public GetArtistsByDateRequest(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
