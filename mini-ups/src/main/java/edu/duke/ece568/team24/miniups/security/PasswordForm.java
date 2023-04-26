package edu.duke.ece568.team24.miniups.security;

import javax.validation.constraints.NotEmpty;

public class PasswordForm {
    @NotEmpty(message = "Password should not be empty")
    private String oldPassword;
    @NotEmpty(message = "Password should not be empty")
    private String newPassword;
    @NotEmpty(message = "Password should not be empty")
    private String confirmPassword;

    public PasswordForm() {
    }

    public PasswordForm(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getnewPassword() {
        return newPassword;
    }

    public void setnewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
