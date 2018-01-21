package ua.glushko.model.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * GuestBook
 */
public class GuestBook implements GenericEntity, Serializable{
    private int id;
    private int orderId;
    private String userName;
    private String decription;
    private Date actionDate;
    private String memo;

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
        return decription;
    }

    public void setDescription(String decription) {
        this.decription = decription;
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

    @Override
    public String toString() {
        return "GuestBook{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", userName='" + userName + '\'' +
                ", description='" + decription + '\'' +
                ", actionDate=" + actionDate +
                ", memo='" + memo + '\'' +
                '}';
    }
}
