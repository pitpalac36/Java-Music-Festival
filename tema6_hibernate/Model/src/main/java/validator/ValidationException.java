package validator;

public class ValidationException extends Exception{
    private final String message;

    @Override
    public String getMessage() {return message;}

    public ValidationException(String message) {this.message = message;}
}