package edu.duke.ece568.team24.miniups.dto;

import java.util.Date;

import edu.duke.ece568.team24.miniups.model.AccountEntity;

public class AccountDto {

    public static AccountDto mapper(AccountEntity accountEntity) {
        if (accountEntity == null) {
            return null;
        } else {
            return new AccountDto(accountEntity.getId(), accountEntity.getUsername(), accountEntity.getEmail(),
                    accountEntity.getRole(), accountEntity.getCreatedTime(), accountEntity.getLastLoggedInTime());
        }
    }

    private Long id;

    private String username;

    private String email;

    private String role;

    private Date createdTime;

    private Date lastLoggedInTime;

    public AccountDto(Long id, String username, String email, String role, Date createdTime, Date lastLoggedInTime) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.createdTime = createdTime;
        this.lastLoggedInTime = lastLoggedInTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastLoggedInTime() {
        return lastLoggedInTime;
    }

    public void setLastLoggedInTime(Date lastLoggedInTime) {
        this.lastLoggedInTime = lastLoggedInTime;
    }

}
