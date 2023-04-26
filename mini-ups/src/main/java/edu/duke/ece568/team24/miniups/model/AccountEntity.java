package edu.duke.ece568.team24.miniups.model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    @SequenceGenerator(name = "account_sequence", sequenceName = "account_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_sequence")
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", unique = true, nullable = true)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "last_logged_in_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoggedInTime;

    @PrePersist
    protected void onCreate() {
        createdTime = new Date();
        lastLoggedInTime = createdTime;
    }

    @PreUpdate
    protected void onUpdate() {
        lastLoggedInTime = new Date();
    }

    public AccountEntity() {
    }

    public AccountEntity(String username, String password, String email, String role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
