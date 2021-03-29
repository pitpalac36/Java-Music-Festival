import domain.User;
import org.junit.Assert;
import org.junit.Test;
import validator.UserValidator;
import validator.ValidationException;
import validator.Validator;

public class TestValidator {
    private static Validator<User> userValidator = new UserValidator();

    @Test(expected = ValidationException.class)
    public void testUserInvalidUsername() throws Exception{
        userValidator.validate(new User(" ", "okPassword"));
    }

    @Test(expected = ValidationException.class)
    public void testUserInvalidPassword() throws Exception{
        userValidator.validate(new User("okUsername", "   "));
    }

    @Test
    public void testUserInvalidCredentials() {
        try {
            userValidator.validate(new User("", "   "));
        }
        catch (ValidationException e) {
            Assert.assertEquals("\nUsername cannot be empty!!\nPassword cannot be empty!!\n", e.getMessage());
            return;
        }
        Assert.fail();
    }

    @Test
    public void testValidUser() {
        try {
            userValidator.validate(new User("okUsername", "okPassword"));
        }
        catch (ValidationException e) {
            Assert.fail();
        }
    }
}
