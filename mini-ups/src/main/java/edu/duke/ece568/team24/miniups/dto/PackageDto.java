package edu.duke.ece568.team24.miniups.dto;

import java.util.Date;

import edu.duke.ece568.team24.miniups.model.PackageEntity;

public class PackageDto {

    public static PackageDto mapper(PackageEntity packageEntity) {
        if (packageEntity == null) {
            return null;
        } else {
            return new PackageDto(packageEntity.getId(), packageEntity.getTrackingNumber(),
                    packageEntity.getDescription(), packageEntity.getStatus(),
                    packageEntity.getOriginX(), packageEntity.getOriginY(), packageEntity.getLoadedTime(),
                    packageEntity.getLastUpdatedTime(),
                    OrderDto.mapper(packageEntity.getOrderEntity()),
                    TruckDto.mapper(packageEntity.getTruckEntity()));
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

    private OrderDto orderDto;

    private TruckDto truckDto;

    private Double remainDistance;

    public PackageDto(Long id, Long trackingNumber, String description, String status, Integer originX, Integer originY,
            Date loadedTime, Date lastUpdatedTime, OrderDto orderDto, TruckDto truckDto) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.description = description;
        this.status = status;
        this.originX = originX;
        this.originY = originY;
        this.loadedTime = loadedTime;
        this.lastUpdatedTime = lastUpdatedTime;
        this.orderDto = orderDto;
        this.truckDto = truckDto;

        this.remainDistance = truckDto == null ? 0
                : Math.sqrt(Math.pow(truckDto.getRealX() - orderDto.getDestinationX(), 2)
                        + Math.pow(truckDto.getRealY() - orderDto.getDestinationY(), 2));
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

    public OrderDto getOrderDto() {
        return orderDto;
    }

    public void setOrderDto(OrderDto orderDto) {
        this.orderDto = orderDto;
    }

    public TruckDto getTruckDto() {
        return truckDto;
    }

    public void setTruckDto(TruckDto truckDto) {
        this.truckDto = truckDto;
    }

    public Double getRemainDistance() {
        return remainDistance;
    }

    public void setRemainDistance(Double remainDistance) {
        this.remainDistance = remainDistance;
    }

}
