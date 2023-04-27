package edu.duke.ece568.team24.miniups.dto;

import java.util.Date;

import edu.duke.ece568.team24.miniups.model.OrderEntity;
import edu.duke.ece568.team24.miniups.model.PackageEntity;
import edu.duke.ece568.team24.miniups.model.TruckEntity;

public class PackageDto {

    public static PackageDto mapper(PackageEntity p) {
        if (p == null) {
            return null;
        } else {
            OrderEntity order = p.getOrderEntity();
            TruckEntity truck = p.getTruckEntity();
            if (truck == null) {
                return new PackageDto(p.getId(), p.getTrackingNumber(),
                        p.getDescription(), p.getStatus(),
                        p.getOriginX(), p.getOriginY(), p.getLoadedTime(),
                        p.getLastUpdatedTime(), order.getId(), 0,
                        order.getDestinationX(), order.getDestinationY(),
                        order.getDestinationX(), order.getDestinationY());
            } else {
                return new PackageDto(p.getId(), p.getTrackingNumber(),
                        p.getDescription(), p.getStatus(),
                        p.getOriginX(), p.getOriginY(), p.getLoadedTime(),
                        p.getLastUpdatedTime(), order.getId(), truck.getId(),
                        truck.getRealX(), truck.getRealY(),
                        order.getDestinationX(), order.getDestinationY());
            }
        }
    }

    private Long id;

    private Long trackingNumber;

    private String description;

    private String status;

    private Integer originX;

    private Integer originY;

    private Date loadedTime;

    private Date lastUpdatedTime;

    private Integer orderId;

    private Integer truckId;

    private Integer currX;

    private Integer currY;

    private Integer destinationX;

    private Integer destinationY;

    private Double remainDistance;

    public PackageDto(Long id, Long trackingNumber, String description, String status, Integer originX, Integer originY,
            Date loadedTime, Date lastUpdatedTime, Integer orderId, Integer truckId, Integer currX, Integer currY,
            Integer destinationX, Integer destinationY) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.description = description;
        this.status = status;
        this.originX = originX;
        this.originY = originY;
        this.loadedTime = loadedTime;
        this.lastUpdatedTime = lastUpdatedTime;
        this.orderId = orderId;
        this.truckId = truckId;
        this.currX = currX;
        this.currY = currY;
        this.destinationX = destinationX;
        this.destinationY = destinationY;

        this.remainDistance = Math.sqrt(Math.pow(currX - destinationX, 2) + Math.pow(currY - destinationY, 2));
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

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getTruckId() {
        return truckId;
    }

    public void setTruckId(Integer truckId) {
        this.truckId = truckId;
    }

    public Integer getCurrX() {
        return currX;
    }

    public void setCurrX(Integer currX) {
        this.currX = currX;
    }

    public Integer getCurrY() {
        return currY;
    }

    public void setCurrY(Integer currY) {
        this.currY = currY;
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

    public Double getRemainDistance() {
        return remainDistance;
    }

    public void setRemainDistance(Double remainDistance) {
        this.remainDistance = remainDistance;
    }

}
