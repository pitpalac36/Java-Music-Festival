package festival.model.validator;

public class InvalidPurchaseException extends Exception{
    private String message;

    public InvalidPurchaseException(int numberOfTicketsWanted, int showId) {
        message = "Cannot buy " + numberOfTicketsWanted + " tickets for show " + showId + " !!";
    }

    public String getMessage() {
        return message;
    }
}
