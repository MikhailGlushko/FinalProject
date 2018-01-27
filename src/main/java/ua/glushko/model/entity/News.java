package ua.glushko.model.entity;

import java.io.Serializable;
import java.util.Date;

/** GuestBook */
public class News implements GenericEntity, Serializable{
    private int id;
    private String description;
    private Date actionDate;
    private String memo;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "News{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", actionDate=" + actionDate +
                ", memo='" + memo + '\'' +
                '}';
    }
}
