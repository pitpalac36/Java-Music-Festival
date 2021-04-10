package validator;

public interface Validator<T> {
    void validate(T elem) throws ValidationException;
}