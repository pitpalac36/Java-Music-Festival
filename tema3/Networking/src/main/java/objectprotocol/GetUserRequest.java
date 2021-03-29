package objectprotocol;

public class GetUserRequest implements Request {
    private String username;
    private String password;

    public GetUserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
