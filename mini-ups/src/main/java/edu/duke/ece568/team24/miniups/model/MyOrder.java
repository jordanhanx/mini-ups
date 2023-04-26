package edu.duke.ece568.team24.miniups.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "myorder")
public class MyOrder {

    @Id
    @SequenceGenerator(name = "order_sequence", sequenceName = "order_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_sequence")
    private Long id;

    @Column(name = "amazon_order_id", unique = true, nullable = false)
    private Integer amazonOrderId;

    @Column(name = "destination_x", nullable = false)
    private Integer destinationX;

    @Column(name = "destination_y", nullable = false)
    private Integer destinationY;

    // @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AccountEntity account;

}
