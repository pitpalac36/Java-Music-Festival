package objectprotocol;

import dtos.UserDto;

public class LoginRequest implements Request {
    private UserDto user;

    public LoginRequest(UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }
}
