package ua.glushko.model.entity;

import ua.glushko.model.dao.Identified;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.model.entity.OrderType;
import ua.glushko.model.entity.User;

import java.io.Serializable;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Order implements GenericEntity, Serializable {
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
        this.descriptionShort = descriptionShort;
    }

    public String getDescriptionDetail() {
        return descriptionDetail;
    }

    public void setDescriptionDetail(String descriptionDetail) {
        this.descriptionDetail = descriptionDetail;
    }

    public int getRepairService() {
        return repairService;
    }

    public void setRepairService(int repairService) {
        this.repairService = repairService;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getAppliance() {
        return appliance;
    }

    public void setAppliance(String appliance) {
        this.appliance = appliance;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public OrderStatus getStatus() {
        return status;
    }


    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setStatus(String status){
        this.status = OrderStatus.valueOf(OrderStatus.class,status);
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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
