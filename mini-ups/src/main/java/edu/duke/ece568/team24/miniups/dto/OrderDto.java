package edu.duke.ece568.team24.miniups.dto;

import java.util.Date;
import java.util.List;

import edu.duke.ece568.team24.miniups.model.OrderEntity;

public class OrderDto {

    public static OrderDto mapper(OrderEntity orderEntity) {
        if (orderEntity == null) {
            return null;
        } else {
            OrderDto orderDto = new OrderDto(orderEntity.getId(), orderEntity.getStatus(),
                    orderEntity.getDestinationX(), orderEntity.getDestinationY(), orderEntity.getOwnerUsername(),
                    orderEntity.getCreatedTime(), orderEntity.getLastUpdatedTime());
            List<PackageDto> packages = orderEntity.getPackages().stream()
                    .sorted((p1, p2) -> p1.getId().compareTo(p2.getId()))
                    .map(PackageDto::mapper)
                    .toList();
            orderDto.setPackages(packages);
            return orderDto;
        }
    }

    private Integer id;

    private String status;

    private Integer destinationX;

    private Integer destinationY;

    private String ownerUsername;

    private Date createdTime;

    private Date lastUpdatedTime;

    private List<PackageDto> packages;

    public OrderDto(Integer id, String status, Integer destinationX, Integer destinationY, String ownerUsername,
            Date createdTime, Date lastUpdatedTime) {
        this.id = id;
        this.status = status;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.ownerUsername = ownerUsername;
        this.createdTime = createdTime;
        this.lastUpdatedTime = lastUpdatedTime;
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

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
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

    public List<PackageDto> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageDto> packages) {
        this.packages = packages;
    }

}
