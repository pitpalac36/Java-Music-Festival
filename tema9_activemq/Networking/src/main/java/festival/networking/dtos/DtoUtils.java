package festival.networking.dtos;
import festival.domain.models.Artist;
import festival.domain.models.Show;
import festival.domain.models.Ticket;
import festival.domain.models.User;

public class DtoUtils {

    public static User getFromDto(UserDto userDto) {
        return new User(userDto.getUsername(), userDto.getPassword());
    }

    public static UserDto getDto(User user) {
        return new UserDto(user.getUsername(), user.getUsername());
    }

    public static Ticket getFromDto(TicketDto ticketDto) {
        return new Ticket(ticketDto.getShowId(), ticketDto.getPurchaserName(), ticketDto.getNumber());
    }

    public static TicketDto getDto(Ticket ticket) {
        return new TicketDto(ticket.getShowId(), ticket.getPurchaserName(), ticket.getNumber());
    }

    public static Show getFromDto(ShowDto showDto) {
        return new Show(showDto.getId(), showDto.getArtistName(), showDto.getDate(),
                showDto.getLocation(), showDto.getAvailableTicketsNumber(), showDto.getSoldTicketsNumber());
    }

    private static Artist getFromDto(ArtistDto dto) {
        return new Artist(dto.getName(), dto.getLocation(), dto.getDate(), dto.getAvailableTicketsNumber());
    }

    private static ArtistDto getDto(Artist artist) {
        return new ArtistDto(artist.getName(), artist.getLocation(), artist.getDate(), artist.getAvailableTicketsNumber());
    }

    public static ShowDto getDto(Show show) {
        return new ShowDto(show.getId(), show.getArtistName(), show.getDate(),
                show.getLocation(), show.getAvailableTicketsNumber(), show.getSoldTicketsNumber());
    }

    public static ArtistDto[] getDto(Artist[] artists) {
        ArtistDto[] artistsDtos = new ArtistDto[artists.length];
        for(int i = 0; i < artists.length; i++) {
            artistsDtos[i] = getDto(artists[i]);
        }
        return artistsDtos;
    }

    public static ShowDto[] getDto(Show[] shows) {
        ShowDto[] showsDtos = new ShowDto[shows.length];
        for(int i = 0; i < shows.length; i++) {
            showsDtos[i] = getDto(shows[i]);
        }
        return showsDtos;
    }

    public static Show[] getFromDto(ShowDto[] dtos) {
        Show[] shows = new Show[dtos.length];
        for(int i = 0; i < dtos.length; i++) {
            shows[i] = getFromDto(dtos[i]);
        }
        return shows;
    }

    public static Artist[] getFromDto(ArtistDto[] dtos) {
        Artist[] artists = new Artist[dtos.length];
        for(int i = 0; i < dtos.length; i++) {
            artists[i] = getFromDto(dtos[i]);
        }
        return artists;
    }

}
