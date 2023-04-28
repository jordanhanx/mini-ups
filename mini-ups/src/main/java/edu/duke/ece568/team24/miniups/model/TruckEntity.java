package edu.duke.ece568.team24.miniups.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trucks")
public class TruckEntity {

    @Id
    @SequenceGenerator(name = "truck_sequence", sequenceName = "truck_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "truck_sequence")
    private Integer id;

    private Integer realX;

    private Integer realY;

    private String status;

    private Integer targetWarehouseId;

    @OneToMany(mappedBy = "truckEntity")
    private List<PackageEntity> packages = new ArrayList<>();

    public TruckEntity() {
        this.realX = 0;
        this.realY = 0;
        this.status = "Created";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRealX() {
        return realX;
    }

    public void setRealX(Integer realX) {
        this.realX = realX;
    }

    public Integer getRealY() {
        return realY;
    }

    public void setRealY(Integer realY) {
        this.realY = realY;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTargetWarehouseId() {
        return targetWarehouseId;
    }

    public void setTargetWarehouseId(Integer targetWarehouseId) {
        this.targetWarehouseId = targetWarehouseId;
    }

    public List<PackageEntity> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageEntity> packages) {
        this.packages = packages;
    }

}
