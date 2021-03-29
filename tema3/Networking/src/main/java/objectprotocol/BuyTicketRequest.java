package objectprotocol;
import dtos.TicketDto;

public class BuyTicketRequest implements Request {
    private TicketDto ticketDto;

    public BuyTicketRequest(TicketDto ticketDto) {
        this.ticketDto = ticketDto;
    }

    public TicketDto getTicket() {
        return ticketDto;
    }
}
