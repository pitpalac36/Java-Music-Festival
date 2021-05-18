package festival.model.validator;

public interface Validator<T> {
    void validate(T elem) throws ValidationException;
}