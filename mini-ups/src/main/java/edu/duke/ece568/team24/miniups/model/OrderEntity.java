package edu.duke.ece568.team24.miniups.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private Integer id;

    private String status;

    private Integer destinationX;

    private Integer destinationY;

    private String ownerUsername;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL)
    private List<PackageEntity> packages = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdTime = new Date();
        lastUpdatedTime = createdTime;
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedTime = new Date();
    }

    public OrderEntity() {
    }

    public OrderEntity(Integer id, Integer destinationX, Integer destinationY,
            String ownerUsername) {
        this.id = id;
        this.status = "created";
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.ownerUsername = ownerUsername;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDestinationX() {
        return destinationX;
    }

    public void setDestinationX(Integer destinationX) {
        this.destinationX = destinationX;
    }

    public Integer getDestinationY() {
        return destinationY;
    }

    public void setDestinationY(Integer destinationY) {
        this.destinationY = destinationY;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public List<PackageEntity> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageEntity> packages) {
        this.packages = packages;
    }
}
