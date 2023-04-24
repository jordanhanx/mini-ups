package edu.duke.ece568.sp.miniups.model;

import javax.persistence.*;
import java.util.List;

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

//    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
//    private List<MyOrder> orders;
    public Account() {

    }

    public Account(String accountName, String password) {
        this.accountName = accountName;
        this.password = password;
    }

    public Account(Long accountID, String accountName, String password) {
        this.accountID = accountID;
        this.accountName = accountName;
        this.password = password;
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
}
