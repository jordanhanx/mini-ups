package edu.duke.ece568.sp.miniups.model;

import javax.persistence.*;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "account_sequence"
    )
    @Column(name = "account_id")
    private Long accountID;
    @Column(name = "account_name", unique = true, nullable = false)
    private String accountName;
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "role", nullable = false)
    private String role;

//    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
//    private List<MyOrder> orders;
    public Account() {

    }

    public Account(String accountName, String password, String email, String role) {
        this.accountName = accountName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Account(Long accountID, String accountName, String password, String email, String role) {
        this.accountID = accountID;
        this.accountName = accountName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public Long getAccountID() {
        return accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getPassword() {
        return password;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
}
