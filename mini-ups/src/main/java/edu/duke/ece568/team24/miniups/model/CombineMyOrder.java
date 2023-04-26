package edu.duke.ece568.team24.miniups.model;

import java.util.List;

public class CombineMyOrder {

    public Long orderID;
    public Integer destinationX;
    public Integer destinationY;

    public List<MyPackage> packages;

    public CombineMyOrder() {
    }

    public CombineMyOrder(Long orderID, Integer destinationX, Integer destinationY, List<MyPackage> packages) {
        this.orderID = orderID;
        this.destinationX = destinationX;
        this.destinationY = destinationY;
        this.packages = packages;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
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

    public List<MyPackage> getPackages() {
        return packages;
    }

    public void setPackages(List<MyPackage> packages) {
        this.packages = packages;
    }
}
