package ua.glushko.utils.tags;

import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;
import ua.glushko.commands.utils.Authentication;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_PAGE;
import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;

@SuppressWarnings("serial")
public class ShowOrdersListTag extends ShowListTag {
    public void makeBody(List<Object> list, StringBuilder builder, Integer rowsCount) {
        // build bode if table
        String command;
        Object attribute = pageContext.getRequest().getAttribute(PARAM_COMMAND);
        if (Objects.nonNull(attribute))
            command = (String) attribute;
        else
            command = pageContext.getRequest().getParameter(PARAM_COMMAND);

        Integer page;
        attribute = pageContext.getRequest().getAttribute(PARAM_PAGE);
        if(Objects.nonNull(attribute))
            page = (Integer)attribute;
        else if(Objects.nonNull(pageContext.getRequest().getParameter(PARAM_PAGE)))
            page = Integer.valueOf(pageContext.getRequest().getParameter(PARAM_PAGE));
        else page=1;

        Integer userId = null;
        attribute = pageContext.getSession().getAttribute(Authentication.PARAM_ID);
        if(Objects.nonNull(attribute))
            userId = (Integer)attribute;

        Iterator<Object> iterator = list.iterator();
        for (int i = 0; i < rowsCount; i++) {
            Order order = null;
            if (!iterator.hasNext())
                continue;
            Object next = iterator.next();
            if (next instanceof Order) {
                order = (Order) next;
            }
            String style="";
            //noinspection ConstantConditions
            if(Objects.nonNull(userId) && Objects.nonNull(order) && order.getUserId()==userId &&
                    (order.getStatus()== OrderStatus.CONFIRMATION || order.getStatus()==OrderStatus.PAYMENT || order.getStatus()==OrderStatus.REJECT)
                    || Objects.nonNull(userId) && order.getEmployeeId()==userId && (order.getStatus()==OrderStatus.VERIFICATION || order.getStatus()==OrderStatus.ESTIMATE || order.getStatus()==OrderStatus.PROGRESS || order.getStatus()==OrderStatus.COMPLETE)){
                style = "class=\"btn-warning\"";
            } else if(order.getStatus()==OrderStatus.NEW) {
                style = "class=\"btn-info\"";
            } else if(Objects.nonNull(userId) && (order.getUserId()==userId || order.getEmployeeId()==userId)){
                style = "class=\"btn-success\"";
            }
            builder.append("<tr onClick=\"window.location.href='")
                    .append(PARAM_SERVLET_PATH)
                    .append("?command=").append(command).append("_detail")
                    .append("&page=").append(page)
                    .append("&order_id=").append(Objects.requireNonNull(order).getId())
                    .append("'; return false\" ").append(style).append(">")
                    .append("<td><a href=\"")
                    .append(PARAM_SERVLET_PATH)
                    .append("?command=").append(command).append("_detail")
                    .append("&page=").append(page)
                    .append("&order_id=").append(order.getId()).append("\">")
                    .append(order.getId())
                    .append("</a></td>");
            builder.append("<td>").append(order.getDescriptionShort()).append("</td>");
            builder.append("<td>").append(order.getStatus()).append("</td>");
            builder.append("<td>").append(order.getOrderDate()).append("</td>");
            builder.append("<td>").append(order.getExpectedDate()).append("</td>");
            builder.append("<td>").append(order.getUserName()).append("</td>");
            builder.append("<td>").append(order.getEmployeeName()).append("</td>");
            builder.append("</tr>");
        }
    }

    public void makeHeader(StringBuilder builder) {
            builder.append("<thead><tr>");
            builder.append("<th>ID</th>").append("<th>DESCRIPTION</th>").append("<th>STATUS</th>").append("<th>ORDER DATE</th>")
            .append("<th>EXPECTED DAY</th>").append("<th>CUSTOMER</th>").append("<th>EMPLOYEE</th>");
            builder.append("</tr></thead>");
    }
}