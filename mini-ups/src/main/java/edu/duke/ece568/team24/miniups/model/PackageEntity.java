package edu.duke.ece568.team24.miniups.model;

import java.util.Date;
import java.util.Random;

import javax.persistence.*;

@Entity
@Table(name = "packages")
public class PackageEntity {

    @Id
    private Long id;

    private Long trackingNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String status;

    private Integer originX;

    private Integer originY;

    @Temporal(TemporalType.TIMESTAMP)
    private Date loadedTime;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_order_id")
    private OrderEntity orderEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_truck_id")
    private TruckEntity truckEntity;

    @PrePersist
    protected void onCreate() {
        long shiftedTimestamp = System.currentTimeMillis() << 17;
        int randomNum = new Random().nextInt(99999);
        trackingNumber = shiftedTimestamp | randomNum;
        loadedTime = new Date();
        lastUpdatedTime = loadedTime;
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdatedTime = new Date();
    }

    public PackageEntity() {
    }

    public PackageEntity(Long id, String description, OrderEntity orderEntity,
            TruckEntity truckEntity) {
        this.id = id;
        this.description = description;
        this.originX = truckEntity.getRealX();
        this.originY = truckEntity.getRealY();
        this.orderEntity = orderEntity;
        this.truckEntity = truckEntity;
        this.status = "loaded";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(Long trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOriginX() {
        return originX;
    }

    public void setOriginX(Integer originX) {
        this.originX = originX;
    }

    public Integer getOriginY() {
        return originY;
    }

    public void setOriginY(Integer originY) {
        this.originY = originY;
    }

    public Date getLoadedTime() {
        return loadedTime;
    }

    public void setLoadedTime(Date loadedTime) {
        this.loadedTime = loadedTime;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public TruckEntity getTruckEntity() {
        return truckEntity;
    }

    public void setTruckEntity(TruckEntity truckEntity) {
        this.truckEntity = truckEntity;
    }

}
