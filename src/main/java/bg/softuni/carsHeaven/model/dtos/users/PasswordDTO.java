package bg.softuni.carsHeaven.model.dtos.users;

import jakarta.validation.constraints.Size;

public class PasswordDTO {

    private String oldPassword;

    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters!")
    private String password;

    private String confirmPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
