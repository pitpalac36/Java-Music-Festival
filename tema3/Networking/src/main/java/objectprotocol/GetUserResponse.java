package objectprotocol;

import dtos.UserDto;

public class GetUserResponse implements Response {
    private UserDto user;

    public GetUserResponse(UserDto user) {
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }
}
