package ua.glushko.model.entity;

import ua.glushko.model.dao.Identified;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.model.entity.OrderType;
import ua.glushko.model.entity.User;

import java.io.Serializable;
import java.sql.Date;

public class Order implements GenericEntity, Serializable {
    private int id;
    private String descriptionShort;
    private String descriptionDetail;
    private int repairService;
    private String city;
    private String street;
    private Date orderDate;
    private String time;
    private String appliance;
    private String paidMethod;
    private int userId;
    private String memo;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppliance() {
        return appliance;
    }

    public void setAppliance(String appliance) {
        this.appliance = appliance;
    }

    public String getPaidMethod() {
        return paidMethod;
    }

    public void setPaidMethod(String paidMethod) {
        this.paidMethod = paidMethod;
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

    @Override
    public Integer getId() {
        return id;
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
                ", time='" + time + '\'' +
                ", appliance='" + appliance + '\'' +
                ", paidMethod='" + paidMethod + '\'' +
                ", userId=" + userId +
                ", memo='" + memo + '\'' +
                '}';
    }
}
