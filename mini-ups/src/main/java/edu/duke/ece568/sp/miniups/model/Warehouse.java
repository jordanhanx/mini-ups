package edu.duke.ece568.sp.miniups.model;

import javax.persistence.*;
import java.util.List;
@Entity
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @SequenceGenerator(
            name = "warehouse_sequence",
            sequenceName = "warehouse_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "warehouse_sequence"
    )
    @Column(name = "warehouse_id")
    private Long warehouseID;

    @Column(name = "x", nullable = false)
    private Integer x;

    @Column(name = "y", nullable = false)
    private Integer y;

    public Warehouse() {
    }

    public Warehouse(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Warehouse(Long warehouseID, Integer x, Integer y) {
        this.warehouseID = warehouseID;
        this.x = x;
        this.y = y;
    }

    public Long getWarehouseID() {
        return warehouseID;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public void setWarehouseID(Long warehouseID) {
        this.warehouseID = warehouseID;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
