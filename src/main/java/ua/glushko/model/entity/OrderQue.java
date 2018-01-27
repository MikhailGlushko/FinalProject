package ua.glushko.model.entity;

import java.util.Date;

public class OrderQue implements GenericEntity {

    private int id;
    private int orderId;
    private UserRole role;
    private int employeeId;
    private Date create;
    private Date close;
    private String message;

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

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setRole(String role){
        this.role = UserRole.valueOf(role);
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getClose() {
        return close;
    }

    public void setClose(Date close) {
        this.close = close;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "OrderQue{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", role=" + role +
                ", employeeId=" + employeeId +
                ", create=" + create +
                ", close=" + close +
                ", message='" + message + '\'' +
                '}';
    }
}
