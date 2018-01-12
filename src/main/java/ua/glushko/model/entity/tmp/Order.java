package ua.glushko.model.entity.tmp;

import ua.glushko.model.dao.Identified;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.model.entity.OrderType;
import ua.glushko.model.entity.User;

import java.sql.Date;

public class Order implements Identified<Integer> {
    private int id;
    private OrderType orderType;
    private User userCreator;
    private User userExecutor;
    private OrderStatus orderStatus;
    private String deviceName;
    private String defectDescription;
    private DevicePhisicalState devicePhisicalState;
    private String additionalInfo;
    private Date createDate;
    private Date modifyDate;
    private Date deadlineDate;
    private boolean urgentStatus;
    private int price;
    private int prepaid;
    private int repairService;
    private int repairDevice;

    @Override
    public Integer getId() {
        return null;
    }

    @Override
    public void setId(Integer id) {

    }
}
