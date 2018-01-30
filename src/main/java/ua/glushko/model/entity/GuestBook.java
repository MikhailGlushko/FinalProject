package ua.glushko.model.entity;

import java.io.Serializable;
import java.util.Date;

/** GuestBook */
public class GuestBook implements GenericEntity, Serializable{
    private int id;
    private int orderId;
    private String userName;
    private String description;
    private Date actionDate;
    private String memo;
    private int userId;

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

        public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "GuestBook{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", userName='" + userName + '\'' +
                ", description='" + description + '\'' +
                ", actionDate=" + actionDate +
                ", memo='" + memo + '\'' +
                ", userId=" + userId +
                '}';
    }
}
