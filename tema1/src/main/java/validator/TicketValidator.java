package validator;

import domain.Ticket;

public class TicketValidator implements Validator<Ticket> {

    @Override
    public void validate(Ticket ticket) throws ValidationException {
        StringBuilder errors = new StringBuilder("");
        if (ticket.getPurchaserName().isEmpty())
            errors.append("\nPurchaser name cannot be empty!!\n");
        if (ticket.getNumber() < 1)
            errors.append("Number of purchased tickets must be at least 1!!\n");
        if (!errors.toString().isEmpty())
            throw new ValidationException(errors.toString());
    }
}
