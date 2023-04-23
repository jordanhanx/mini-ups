package edu.duke.ece568.sp.miniups.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "myorder")
public class MyOrder {

    @Id
    @SequenceGenerator(
            name = "order_sequence",
            sequenceName = "order_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "order_sequence"
    )
    @Column(name = "order_id")
    private Long orderID;
    @Column(name = "destination_x", nullable = false)
    private Integer destinationX;

    @Column(name = "destination_y", nullable = false)
    private Integer destinationY;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account account;

    public MyOrder() {
    }

    public MyOrder(Integer destinationX, Integer destinationY, Account account) {
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.account = account;
    }

//    public MyOrder(Long orderID, Integer destinationX, Integer destinationY, Account account) {
//        this.orderID = orderID;
//        this.destinationX = destinationX;
//        this.destinationY = destinationY;
//        this.account = account;
//    }

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
}
