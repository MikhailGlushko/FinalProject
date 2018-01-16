package ua.glushko.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Order History
 */
public class OrderHistory implements GenericEntity, Serializable{
    private int id;
    private int orderId;
    private int userId;
    private String action;
    private String decription;
    private Date actionDate;
    private String oldValue;
    private String newValue;
    private String userName;

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDecription() {
        return decription;
    }

    public void setDecription(String decription) {
        this.decription = decription;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "OrderHistory{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", userId=" + userId +
                ", action='" + action + '\'' +
                ", decription='" + decription + '\'' +
                ", actionDate=" + actionDate +
                ", oldValue='" + oldValue + '\'' +
                ", newValue='" + newValue + '\'' +
                '}';
    }
}
