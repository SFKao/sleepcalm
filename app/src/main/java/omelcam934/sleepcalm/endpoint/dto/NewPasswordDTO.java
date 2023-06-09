package omelcam934.sleepcalm.endpoint.dto;

public class NewPasswordDTO {

    public String email;
    public String lastPassword;
    public String newPassword;

    public NewPasswordDTO(String email, String lastPassword, String newPassword) {
        this.email = email;
        this.lastPassword = lastPassword;
        this.newPassword = newPassword;
    }
}
