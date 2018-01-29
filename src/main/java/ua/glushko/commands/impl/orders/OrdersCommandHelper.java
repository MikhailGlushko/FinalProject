package ua.glushko.commands.impl.orders;

import ua.glushko.exception.DaoException;
import ua.glushko.exception.ParameterException;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.services.impl.OrdersService;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static ua.glushko.commands.utils.Authentication.PARAM_NAME_NAME;

public class OrdersCommandHelper {

    public static final String PARAM_ORDER_ID = "order_id";
    public static final String PARAM_ORDER_DESC_SHORT = "order_description_short";
    public static final String PARAM_ORDER_DESC_DETAIL = "order_description_detail";
    public static final String PARAM_ORDER_SERVICE = "order_repair_service";
    public static final String PARAM_ORDER_CITY = "order_city";
    public static final String PARAM_ORDER_STREET = "order_street";
    public static final String PARAM_ORDER_DATE = "order_order_date";
    public static final String PARAM_ORDER_EXPECTED_DATE = "order_expected_date";
    public static final String PARAM_ORDER_APPL = "order_appliance";
    public static final String PARAM_ORDER_PRICE = "order_price";
    public static final String PARAM_ORDER_USER_ID = "order_user_id";
    public static final String PARAM_ORDER_USER_NAME = "order_user_name";
    public static final String PARAM_ORDER_MEMO = "order_memo";
    public static final String PARAM_ORDER_MEMO_CHANGE = "order_memo_change";
    public static final String PARAM_ORDER_STATUS = "order_status";
    public static final String PARAM_ORDER_STATUS_CHANGE = "order_status_change";
    public static final String PARAM_ORDER_EMPLOYEE_ID = "order_employee_id";
    public static final String PARAM_ORDER_EMPLOYEE_ID_CHANGE = "order_employee_id_change";
    public static final String PARAM_ORDER_EMPLOYEE_NAME = "order_employee_name";

    public static final String PARAM_ORDER_FORM_ACTION = "action";
    public static final String PARAM_ORDER = "orders_detail";
    public static final String PARAM_ORDERS_SLIST = "orders_slist";
    public static final String PARAM_ORDERS_LIST = "orders_list";
    public static final String PARAM_ORDERS_LIST_TITLE = "orders_list_head";
    public static final String PARAM_ORDERS_COUNT_NEW  = "orders_count_new";
    public static final String PARAM_ORDERS_STAT_TOTAL = "stat_total";
    public static final String PARAM_ORDERS_STAT   = "order_statuses";
    public static final String PARAM_SERVICES_LIST = "services_list";
    public static final String PARAM_EMPLOYEES_LIST = "employee_list";

    public static final String PARAM_ORDER_ACTION = "action";
    public static final String PARAM_ORDER_ACTION_SAVE = "save";
    public static final String PARAM_ORDER_ACTION_ADD = "add";
    public static final String PARAM_ORDER_ACTION_DELETE = "delete";
    public static final String PARAM_ORDER_ACTION_APPROVE = "approve";
    public static final String PARAM_ORDER_ACTION_REJECT = "reject";
    public static final String PARAM_ORDER_ACTION_COMMENT = "comment";

    public static final String PATH_PAGE_ORDERS = "path.page.orders";
    public static final String PATH_PAGE_ORDERS_DETAIL = "path.page.ordersdetail";
    public static final String PATH_PAGE_ORDERS_ADD = "path.page.ordersadd";

    public static Order getValidatedOrderBeforeUpdate(HttpServletRequest request) throws DaoException, ParameterException {
        String  orderDescriptionShort = request.getParameter(OrdersCommandHelper.PARAM_ORDER_DESC_SHORT);
        String  orderDescriptionDetail = request.getParameter(OrdersCommandHelper.PARAM_ORDER_DESC_DETAIL);
        String  orderCity = request.getParameter(OrdersCommandHelper.PARAM_ORDER_CITY);
        String  orderStreet = request.getParameter(OrdersCommandHelper.PARAM_ORDER_STREET);
        String  orderExpectedDate = request.getParameter(OrdersCommandHelper.PARAM_ORDER_EXPECTED_DATE);
        String  orderAppliance = request.getParameter(OrdersCommandHelper.PARAM_ORDER_APPL);
        String  orderMemo = request.getParameter(OrdersCommandHelper.PARAM_ORDER_MEMO);
        Integer orderEmployeeId = null;
        if(Objects.nonNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_EMPLOYEE_ID)))
            orderEmployeeId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_EMPLOYEE_ID));
        String  orderStatus = request.getParameter(OrdersCommandHelper.PARAM_ORDER_STATUS);
        Double orderPrice = null;
        if(Objects.nonNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_PRICE)))
            orderPrice = Double.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_PRICE));

        if(Objects.isNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID)))
            throw new ParameterException("order.id.not present");
        Integer orderId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));
        if(Objects.isNull(orderDescriptionShort))
            throw new ParameterException("order.description.short.not.present");
        if(Objects.isNull(orderDescriptionDetail))
            throw new ParameterException("0rder.description.detail.not.present");
        if(Objects.isNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_SERVICE)))
            throw new ParameterException("order.repair.service.not.present");
        Integer  orderRepairService = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_SERVICE));
        if(Objects.isNull(orderCity))
            throw new ParameterException("order.city.not.present");
        if(Objects.isNull(orderStreet))
            throw new ParameterException("order.street.not.present");
        if(Objects.isNull(orderAppliance))
            throw new ParameterException("order.appliance.not.present");
        if(Objects.isNull(request.getParameter(OrdersCommandHelper.PARAM_ORDER_USER_ID)))
            throw new ParameterException("order.user.id.not.present");
        Integer orderUserId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_USER_ID));
        if(Objects.isNull(orderStatus))
            throw new ParameterException("order.status.not.present");

        OrdersService ordersService= OrdersService.getService();
        // get user data from database
        Order item = ordersService.getOrderById(orderId);
        //validateAccessToUdate(Authentication.getCurrentUserId(request.getSession()));
        item.setDescriptionShort(orderDescriptionShort);
        item.setDescriptionDetail(orderDescriptionDetail);
        item.setRepairService(orderRepairService);
        item.setCity(orderCity);
        item.setStreet(orderStreet);
        item.setAppliance(orderAppliance);
        item.setUserId(orderUserId);
        item.setMemo(orderMemo);
        item.setStatus(orderStatus);
        if(Objects.nonNull(orderEmployeeId))
            item.setEmployeeId(orderEmployeeId);
        if(Objects.nonNull(orderPrice))
            item.setPrice(orderPrice);
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        //noinspection EmptyCatchBlock
        try {
            date = format.parse(orderExpectedDate);
        } catch (ParseException e) {
        }
        item.setExpectedDate(date);

        return item;
    }

    public static Order getValidatedOrderBeforeCreate(HttpServletRequest request) throws ParameterException {
        // getting data from form
        Order order;
        String  orderDescriptionShort = request.getParameter(OrdersCommandHelper.PARAM_ORDER_DESC_SHORT);
        String  orderDescriptionDetail = request.getParameter(OrdersCommandHelper.PARAM_ORDER_DESC_DETAIL);
        Integer  orderRepairService = null;
        Integer  orderUserId = null;
        //noinspection EmptyCatchBlock
        try {
            orderRepairService = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_SERVICE));
            orderUserId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_USER_ID));
        }catch (NumberFormatException e){
        }
        String  orderCity = request.getParameter(OrdersCommandHelper.PARAM_ORDER_CITY);

        String  orderStreet = request.getParameter(OrdersCommandHelper.PARAM_ORDER_STREET);
        String  orderExpectedDate = request.getParameter(OrdersCommandHelper.PARAM_ORDER_EXPECTED_DATE);
        String  orderAppliance = request.getParameter(OrdersCommandHelper.PARAM_ORDER_APPL);
        String  orderMemo = request.getParameter(OrdersCommandHelper.PARAM_ORDER_APPL);

        if(Objects.isNull(orderDescriptionShort))
            throw new ParameterException("order.description.short.not.present");
        if(Objects.isNull(orderDescriptionDetail))
            throw new ParameterException("order.description.deail.not.present");
        if(Objects.isNull(orderRepairService))
            throw new ParameterException("order.repair.service.not.present");
        if(Objects.isNull(orderUserId))
            throw new ParameterException("order.user.id.not.present");
        if(Objects.isNull(orderCity))
            throw new ParameterException("order.city.not.present");
        if(Objects.isNull(orderStreet))
            throw new ParameterException("order.street.not.present");
        if(Objects.isNull(orderAppliance))
            throw new ParameterException("order.appliance.not.present");

        order = new Order();
        order.setDescriptionShort(orderDescriptionShort);
        order.setDescriptionDetail(orderDescriptionDetail);
        order.setRepairService(orderRepairService);
        order.setCity(orderCity);
        order.setStreet(orderStreet);

        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date date = null;
        //noinspection EmptyCatchBlock
        try {
            date = format.parse(orderExpectedDate);
        } catch (ParseException e) {
        }
        order.setExpectedDate(date);
        order.setAppliance(orderAppliance);
        order.setUserId(orderUserId);
        order.setMemo(orderMemo);

        return order;
    }

    public static Order getValidatedOrderBeforeChangeStatus(HttpServletRequest request) throws ParameterException, DaoException {
        Integer orderId = Integer.valueOf(request.getParameter(OrdersCommandHelper.PARAM_ORDER_ID));
        OrdersService ordersService= OrdersService.getService();
        Order order = ordersService.getOrderById(orderId);

        String action = request.getParameter(PARAM_ORDER_FORM_ACTION);
        String actionMemo = request.getParameter(PARAM_ORDER_MEMO_CHANGE);
        String userName = (String) request.getSession().getAttribute(PARAM_NAME_NAME);
        String newStatus = request.getParameter(PARAM_ORDER_STATUS_CHANGE);
        String newEmployeeId = request.getParameter(PARAM_ORDER_EMPLOYEE_ID_CHANGE);

        if(Objects.isNull(action))
            throw new ParameterException("action.not.presemt");
        if(Objects.isNull(actionMemo))
            throw new ParameterException("action.memo.not.present");
        if(Objects.isNull(userName))
            throw new ParameterException("user.name.not.present");

        switch (action){
            case PARAM_ORDER_ACTION_APPROVE:
                if(Objects.isNull(newStatus))
                    throw new ParameterException("new.status.not.present");
                if(order.getStatus()== OrderStatus.VERIFICATION && (Objects.isNull(newEmployeeId) || newEmployeeId.isEmpty()))
                    throw new ParameterException("new.employee.id.not.present");
                if(order.getStatus()== OrderStatus.valueOf(newStatus))
                    throw new ParameterException("same.status.in.approve.not.allowed");
                order.setStatus(OrderStatus.valueOf(newStatus));
                if(order.getStatus()==OrderStatus.COMPLETE && order.getManagerId()!=0)
                    order.setEmployeeId(order.getManagerId());
                if(Objects.nonNull(newEmployeeId) && !newEmployeeId.isEmpty())
                    order.setEmployeeId(Integer.valueOf(newEmployeeId));
                String oldMemo = order.getMemo();
                if(Objects.isNull(oldMemo))
                    oldMemo="";
                //noinspection StringBufferReplaceableByString
                order.setMemo(new StringBuilder(oldMemo).append("\n")
                        .append(new Date(System.currentTimeMillis())).append(" ")
                        .append(userName).append(" ")
                        .append(action).append(" ")
                        .append(order.getStatus())
                        .append(actionMemo).toString());
                break;
            case PARAM_ORDER_ACTION_REJECT:
                order.setEmployeeId(order.getUserId());
                order.setStatus(OrderStatus.REJECT);
                oldMemo = order.getMemo();
                if(Objects.isNull(oldMemo))
                    oldMemo="";
                //noinspection StringBufferReplaceableByString
                order.setMemo(new StringBuilder(oldMemo).append("\n")
                        .append(new Date(System.currentTimeMillis())).append(" ")
                        .append(userName).append(" ")
                        .append(action).append(" ")
                        .append(actionMemo).toString());
                break;
            case PARAM_ORDER_ACTION_COMMENT:
                oldMemo = order.getMemo();
                if(Objects.isNull(oldMemo))
                    oldMemo="";
                //noinspection StringBufferReplaceableByString
                order.setMemo(new StringBuilder(oldMemo).append("\n")
                        .append(new Date(System.currentTimeMillis())).append(" ")
                        .append(userName).append(" ")
                        .append(action).append(" ")
                        .append(actionMemo).toString());
                break;
            default:
                throw new ParameterException("unknown.command");
        }

        return order;
    }
}
