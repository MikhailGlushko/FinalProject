package ua.glushko.model.entity;

import ua.glushko.model.dao.Identified;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.model.entity.OrderType;
import ua.glushko.model.entity.User;

import java.io.Serializable;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Order implements GenericEntity, Serializable {
    private boolean chanched = false;
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
            this.chanched = true;
        }
    }

    public String getDescriptionDetail() {
        return descriptionDetail;
    }

    public void setDescriptionDetail(String descriptionDetail) {
        if(this.descriptionDetail!=null && !this.descriptionDetail.equals(descriptionDetail) ||
        descriptionDetail!=null && !descriptionDetail.equals(this.descriptionDetail)) {
            this.chanched = true;
            this.descriptionDetail = descriptionDetail;
        }
    }

    public int getRepairService() {
        return repairService;
    }

    public void setRepairService(int repairService) {
        if(this.repairService!=repairService) {
            this.repairService = repairService;
            this.chanched = true;
        }
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if(this.city!=null && !this.city.equals(city) ||
                city!=null && !city.equals(this.city)) {
            this.city = city;
            this.chanched = true;
        }
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if(this.street!=null && !this.street.equals(street) ||
                street!=null && !street.equals(this.street)) {
            this.street = street;
            this.chanched = true;
        }
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        if(this.orderDate!=null && this.orderDate.equals(orderDate) ||
                orderDate!=null && !orderDate.equals(this.orderDate)) {
            this.orderDate = orderDate;
            this.chanched = true;
        }
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        if(this.expectedDate!=null && !this.expectedDate.equals(expectedDate) ||
                expectedDate!=null && !expectedDate.equals(this.expectedDate)) {
            this.expectedDate = expectedDate;
            this.chanched = true;
        }
    }

    public String getAppliance() {
        return appliance;
    }

    public void setAppliance(String appliance) {
        if(this.appliance!=null && this.appliance.equals(appliance) ||
                appliance!=null && !appliance.equals(this.appliance)) {
            this.appliance = appliance;
            this.chanched = true;
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(!Double.valueOf(this.price).equals(Double.valueOf(price))) {
            this.price = price;
            this.chanched = true;
        }
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        if(this.userId!=userId) {
            this.userId = userId;
            this.chanched = true;
        }
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        if(this.memo!=null && !this.memo.equals(memo) ||
                memo!=null && !memo.equals(this.memo)) {
            this.memo = memo;
            this.chanched = true;
        }
    }

    public OrderStatus getStatus() {
        return status;
    }


    public void setStatus(OrderStatus status) {
        if(this.status!=null && !this.status.equals(status) ||
                status!=null && !status.equals(this.status)) {
            this.status = status;
            this.chanched = true;
        }
    }

    public void setStatus(String status){
        if(this.status!=null && !this.status.equals(status) ||
                status!=null && !status.equals(this.status)) {
            this.status = OrderStatus.valueOf(OrderStatus.class, status);
            this.chanched = true;
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
            this.chanched = true;
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        if(this.userName!=null && !this.userName.equals(userName) ||
                userName!=null && !userName.equals(this.userName)) {
            this.userName = userName;
            this.chanched = true;
        }
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        if(this.employeeName!=null && this.employeeName.equals(employeeName) ||
                employeeName!=null && !employeeName.equals(this.employeeName)) {
            this.employeeName = employeeName;
            this.chanched = true;
        }
    }

    public boolean isChanched() {
        return chanched;
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
