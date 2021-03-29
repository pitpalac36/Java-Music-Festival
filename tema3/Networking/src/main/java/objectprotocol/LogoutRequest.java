package objectprotocol;

import dtos.UserDto;

public class LogoutRequest implements Request {
    private UserDto user;

    public LogoutRequest(UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }
}
