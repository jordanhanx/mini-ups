package edu.duke.ece568.team24.miniups.security;

import javax.validation.constraints.Email;

public class EmailForm {
    @Email(message = "Email format invalid")
    private String email;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
