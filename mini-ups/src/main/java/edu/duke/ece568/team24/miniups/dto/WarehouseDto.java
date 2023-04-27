package edu.duke.ece568.team24.miniups.dto;

import edu.duke.ece568.team24.miniups.model.WarehouseEntity;

public class WarehouseDto {

    public static WarehouseDto mapper(WarehouseEntity warehouseEntity) {
        if (warehouseEntity == null) {
            return null;
        } else {
            return new WarehouseDto(warehouseEntity.getId(), warehouseEntity.getX(), warehouseEntity.getY());
        }
    }

    private Integer id;

    private Integer x;

    private Integer y;

    public WarehouseDto(Integer id, Integer x, Integer y) {
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
