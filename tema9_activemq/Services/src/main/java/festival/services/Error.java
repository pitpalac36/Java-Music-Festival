package festival.services;

public class Error extends Exception{

    public Error() {}

    public Error(String message) {
        super(message);
    }
}