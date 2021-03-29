package objectprotocol;
import dtos.ShowDto;

public class GetShowsResponse implements Response {
    private ShowDto[] shows;

    public GetShowsResponse(ShowDto[] shows) {this.shows = shows;}

    public ShowDto[] getShows() {return shows;}
}
