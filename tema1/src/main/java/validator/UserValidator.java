package validator;
import domain.User;

public class UserValidator implements Validator<User> {

    @Override
    public void validate(User user) throws ValidationException {
        StringBuilder errors = new StringBuilder("");
        if (user.getUsername().trim().isEmpty())
            errors.append("\nUsername cannot be empty!!\n");
        if (user.getPassword().trim().isEmpty())
            errors.append("Password cannot be empty!!\n");
        if (!errors.toString().isEmpty())
            throw new ValidationException(errors.toString());
    }

}
