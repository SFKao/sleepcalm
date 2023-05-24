package omelcam934.sleepcalm.endpoint.dto;

import java.io.Serializable;
import java.util.Objects;

public class LoginDto implements Serializable {

    private final String usernameOrEmail;
    private final String password;

    public LoginDto(String usernameOrEmail, String password){
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginDto)) return false;
        LoginDto loginDto = (LoginDto) o;
        return Objects.equals(usernameOrEmail, loginDto.usernameOrEmail) && Objects.equals(password, loginDto.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(usernameOrEmail, password);
    }

    @Override
    public String toString() {
        return "LoginDto{" +
                "usernameOrEmail='" + usernameOrEmail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
