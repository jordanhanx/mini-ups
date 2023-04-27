package edu.duke.ece568.team24.miniups.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "warehouses")
public class WarehouseEntity {

    @Id
    private Integer id;

    private Integer x;

    private Integer y;

    @OneToMany(mappedBy = "targetWareHouse")
    private List<TruckEntity> trucks = new ArrayList<>();

    public WarehouseEntity() {
    }

    public WarehouseEntity(Integer id, Integer x, Integer y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

}
