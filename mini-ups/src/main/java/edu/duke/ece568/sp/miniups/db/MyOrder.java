package edu.duke.ece568.sp.miniups.db;

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
    private Long id;
    private String name;

    public MyOrder() {
    }

    public MyOrder(String name){
        this.name = name;
    }

    public String getName(){ return this.name; }
}
