package edu.duke.ece568.team24.miniups.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "myorder")
public class MyOrder {

    @Id
    // @SequenceGenerator(
    // name = "order_sequence",
    // sequenceName = "order_sequence",
    // allocationSize = 1
    // )
    // @GeneratedValue(
    // strategy = GenerationType.SEQUENCE,
    // generator = "order_sequence"
    // )
    @Column(name = "order_id", unique = true, nullable = false)
    private Long orderID;
    @Column(name = "destination_x", nullable = false)
    private Integer destinationX;

    @Column(name = "destination_y", nullable = false)
    private Integer destinationY;

<<<<<<< HEAD:mini-ups/src/main/java/edu/duke/ece568/sp/miniups/model/MyOrder.java
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
=======
    // @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY)
>>>>>>> origin/main:mini-ups/src/main/java/edu/duke/ece568/team24/miniups/model/MyOrder.java
    @JoinColumn(name = "account_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account account;

    public MyOrder() {
    }

    // public MyOrder(Integer destinationX, Integer destinationY, Account account) {
    // this.destinationX = destinationX;
    // this.destinationY = destinationY;
    // this.account = account;
    // }

    public MyOrder(Long orderID, Integer destinationX, Integer destinationY, Account account) {
        this.orderID = orderID;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.account = account;
    }

    public Long getOrderID() {
        return orderID;
    }

    public Integer getDestinationX() {
        return destinationX;
    }

    public Integer getDestinationY() {
        return destinationY;
    }

    public Account getAccount() {
        return account;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public void setDestinationX(Integer destinationX) {
        this.destinationX = destinationX;
    }

    public void setDestinationY(Integer destinationY) {
        this.destinationY = destinationY;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
