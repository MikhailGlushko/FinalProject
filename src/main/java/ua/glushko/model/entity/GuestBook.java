package ua.glushko.model.entity;

import java.io.Serializable;
import java.util.Date;

public class GuestBook implements GenericEntity, Serializable{
    private int id;
    private int orderId;
    private int userId;
    private String decription;
    private Date date;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "GuestBook{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", decription='" + decription + '\'' +
                ", date=" + date +
                '}';
    }
}
