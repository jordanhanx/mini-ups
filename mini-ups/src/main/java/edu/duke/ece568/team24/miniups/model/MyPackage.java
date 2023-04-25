package edu.duke.ece568.team24.miniups.model;

import edu.duke.ece568.team24.miniups.model.myenum.MyPackageStatus;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "package")
public class MyPackage {

    @Id
    @SequenceGenerator(name = "package_sequence", sequenceName = "package_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "package_sequence")
    @Column(name = "package_id") // tracking number
    private Long packageID;

    @Column(name = "package_id_from_amazon", unique = true, nullable = false)
    private Integer packagefromAmazonID;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MyPackageStatus status;

    @Column(name = "origin_x", nullable = false)
    private Integer originX;

    @Column(name = "origin_y", nullable = false)
    private Integer originY;

    // @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // 如果order被删 package也被删
//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MyOrder myorder;

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "truck_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE) // 待定
    private Truck truck;

    public MyPackage() {
    }

<<<<<<< HEAD:mini-ups/src/main/java/edu/duke/ece568/sp/miniups/model/MyPackage.java
    public MyPackage(Integer packagefromAmazonID, String description, MyPackageStatus status, Integer originX, Integer originY, MyOrder myorder, Truck truck) {
        this.packagefromAmazonID = packagefromAmazonID;
=======
    public MyPackage(String description, MyPackageStatus status, Integer originX, Integer originY, MyOrder myorder,
            Truck truck) {
>>>>>>> origin/main:mini-ups/src/main/java/edu/duke/ece568/team24/miniups/model/MyPackage.java
        this.description = description;
        this.status = status;
        this.originX = originX;
        this.originY = originY;
        this.myorder = myorder;
        this.truck = truck;
    }

<<<<<<< HEAD:mini-ups/src/main/java/edu/duke/ece568/sp/miniups/model/MyPackage.java
    public MyPackage(Long packageID, Integer packagefromAmazonID, String description, MyPackageStatus status, Integer originX, Integer originY, MyOrder myorder, Truck truck) {
=======
    public MyPackage(Long packageID, String description, MyPackageStatus status, Integer originX, Integer originY,
            MyOrder myorder, Truck truck) {
>>>>>>> origin/main:mini-ups/src/main/java/edu/duke/ece568/team24/miniups/model/MyPackage.java
        this.packageID = packageID;
        this.packagefromAmazonID = packagefromAmazonID;
        this.description = description;
        this.status = status;
        this.originX = originX;
        this.originY = originY;
        this.myorder = myorder;
        this.truck = truck;
    }

    public Long getPackageID() {
        return packageID;
    }

    public String getDescription() {
        return description;
    }

    public MyOrder getMyorder() {
        return myorder;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setPackageID(Long packageID) {
        this.packageID = packageID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMyorder(MyOrder myorder) {
        this.myorder = myorder;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public MyPackageStatus getStatus() {
        return status;
    }

    public void setStatus(MyPackageStatus status) {
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

    public Integer getPackagefromAmazonID() {
        return packagefromAmazonID;
    }

    public void setPackagefromAmazonID(Integer packagefromAmazonID) {
        this.packagefromAmazonID = packagefromAmazonID;
    }
}
