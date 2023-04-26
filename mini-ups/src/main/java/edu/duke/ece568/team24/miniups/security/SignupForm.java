package edu.duke.ece568.team24.miniups.security;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class SignupForm {
    @NotEmpty(message = "Username should not be empty")
    private String username;
    @Email(message = "Email format invalid")
    private String email;
    @NotEmpty(message = "Password should not be empty")
    private String password;
    @NotEmpty(message = "Confirm password should not be empty")
    private String confirmPassword;

    public SignupForm() {
    }

    public SignupForm(String username, String email, String password, String confirmPassword) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
