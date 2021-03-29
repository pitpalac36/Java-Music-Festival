package objectprotocol;

import domain.Artist;
import dtos.ArtistDto;

public class GetArtistsByDateResponse implements Response {
    private ArtistDto[] artists;

    public GetArtistsByDateResponse(ArtistDto[] artists) {
        this.artists = artists;
    }

    public ArtistDto[] getArtists() {
        return artists;
    }
}
