package edu.duke.ece568.team24.miniups.dto;

import edu.duke.ece568.team24.miniups.model.TruckEntity;

public class TruckDto {

    public static TruckDto mapper(TruckEntity truckEntity) {
        if (truckEntity == null) {
            return null;
        } else {
            return new TruckDto(truckEntity.getId(), truckEntity.getRealX(),
                    truckEntity.getRealY(), truckEntity.getStatus(),
                    truckEntity.getTargetWarehouseId());
        }
    }

    private Integer id;

    private Integer realX;

    private Integer realY;

    private String status;

    private Integer targetWarehouseId;

    public TruckDto(Integer id, Integer realX, Integer realY, String status, Integer targetWarehouseId) {
        this.id = id;
        this.realX = realX;
        this.realY = realY;
        this.status = status;
        this.targetWarehouseId = targetWarehouseId;
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

}
