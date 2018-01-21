package ua.glushko.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Orders
 */
public class Order implements GenericEntity, Serializable {
    private boolean changed = false;
    private int id;
    private String descriptionShort;
    private String descriptionDetail;
    private int repairService;
    private String city;
    private String street;
    private Date orderDate = new Date(System.currentTimeMillis());
    private Date expectedDate;
    private String appliance;
    private double price;
    private int userId;
    private String userName;
    private String memo;
    private OrderStatus status = OrderStatus.NEW;
    private int employeeId;
    private String employeeName;

    public void setId(int id) {
        this.id = id;
    }

    public String getDescriptionShort() {
        return descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort) {
        if(this.descriptionShort!=null && !this.descriptionShort.equals(descriptionShort) ||
                descriptionShort!=null && !descriptionShort.equals(this.descriptionShort)) {
            this.descriptionShort = descriptionShort;
            this.changed = true;
        }
    }

    public String getDescriptionDetail() {
        return descriptionDetail;
    }

    public void setDescriptionDetail(String descriptionDetail) {
        if(this.descriptionDetail!=null && !this.descriptionDetail.equals(descriptionDetail) ||
        descriptionDetail!=null && !descriptionDetail.equals(this.descriptionDetail)) {
            this.changed = true;
            this.descriptionDetail = descriptionDetail;
        }
    }

    public int getRepairService() {
        return repairService;
    }

    public void setRepairService(int repairService) {
        if(this.repairService!=repairService) {
            this.repairService = repairService;
            this.changed = true;
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if(this.city!=null && !this.city.equals(city) ||
                city!=null && !city.equals(this.city)) {
            this.city = city;
            this.changed = true;
        }
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if(this.street!=null && !this.street.equals(street) ||
                street!=null && !street.equals(this.street)) {
            this.street = street;
            this.changed = true;
        }
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        if(this.orderDate!=null && this.orderDate.equals(orderDate) ||
                orderDate!=null && !orderDate.equals(this.orderDate)) {
            this.orderDate = orderDate;
            this.changed = true;
        }
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        if(this.expectedDate!=null && !this.expectedDate.equals(expectedDate) ||
                expectedDate!=null && !expectedDate.equals(this.expectedDate)) {
            this.expectedDate = expectedDate;
            this.changed = true;
        }
    }

    public String getAppliance() {
        return appliance;
    }

    public void setAppliance(String appliance) {
        if(this.appliance!=null && this.appliance.equals(appliance) ||
                appliance!=null && !appliance.equals(this.appliance)) {
            this.appliance = appliance;
            this.changed = true;
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(!Double.valueOf(this.price).equals(Double.valueOf(price))) {
            this.price = price;
            this.changed = true;
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if(this.userId!=userId) {
            this.userId = userId;
            this.changed = true;
        }
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        if(this.memo!=null && !this.memo.equals(memo) ||
                memo!=null && !memo.equals(this.memo)) {
            this.memo = memo;
            this.changed = true;
        }
    }

    public OrderStatus getStatus() {
        return status;
    }


    public void setStatus(OrderStatus status) {
        if(this.status!=null && !this.status.equals(status) ||
                status!=null && !status.equals(this.status)) {
            this.status = status;
            this.changed = true;
        }
    }

    public void setStatus(String status){
        if(this.status!=null && !this.status.equals(status) ||
                status!=null && !status.equals(this.status)) {
            this.status = OrderStatus.valueOf(OrderStatus.class, status);
            this.changed = true;
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        if(this.employeeId!=employeeId) {
            this.employeeId = employeeId;
            this.changed = true;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if(this.userName!=null && !this.userName.equals(userName) ||
                userName!=null && !userName.equals(this.userName)) {
            this.userName = userName;
            this.changed = true;
        }
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        if(this.employeeName!=null && this.employeeName.equals(employeeName) ||
                employeeName!=null && !employeeName.equals(this.employeeName)) {
            this.employeeName = employeeName;
            this.changed = true;
        }
    }

    public boolean isChanged() {
        return changed;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", descriptionShort='" + descriptionShort + '\'' +
                ", descriptionDetail='" + descriptionDetail + '\'' +
                ", repairService=" + repairService +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", orderDate=" + orderDate +
                ", expectedDate=" + expectedDate +
                ", appliance='" + appliance + '\'' +
                ", price=" + price +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", memo='" + memo + '\'' +
                ", status=" + status +
                ", employeeId=" + employeeId +
                ", employeeName='" + employeeName + '\'' +
                '}';
    }
}
