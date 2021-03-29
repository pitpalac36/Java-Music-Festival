package objectprotocol;
import dtos.TicketDto;

public class BuyTicketResponse implements UpdateResponse {
    private TicketDto ticket;

    public BuyTicketResponse(TicketDto ticket) {
        this.ticket = ticket;
    }

    public TicketDto getTicket() {
        return ticket;
    }
}
