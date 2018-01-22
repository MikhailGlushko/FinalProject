package ua.glushko.tags;

import ua.glushko.model.entity.Order;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_PAGE;

@SuppressWarnings("serial")
public class ShowOrdersListTag extends ShowListTag {
    public void makeBody(List<Object> list, StringBuilder builder, Integer rowsCount) {
        // build bode if table
        String command = null;
        Object attribute = pageContext.getRequest().getAttribute(PARAM_COMMAND);
        if (Objects.nonNull(attribute))
            command = (String) attribute;
        else
            command = pageContext.getRequest().getParameter(PARAM_COMMAND);

        Integer page = null;
        attribute = pageContext.getRequest().getAttribute(PARAM_PAGE);
        if(Objects.nonNull(attribute))
            page = (Integer)attribute;
        else
            page = Integer.valueOf(pageContext.getRequest().getParameter(PARAM_PAGE));

        Iterator<Object> iterator = list.iterator();
        for (int i = 0; i < rowsCount; i++) {
            Order object = null;
            if (!iterator.hasNext())
                continue;
            Object next = iterator.next();
            if (next instanceof Order) {
                object = (Order) next;
            }
            builder.append("<tr onClick=\"window.location.href='")
                    .append("/do?command=").append(command).append("_detail")
                    .append("&page=").append(page)
                    .append("&order_id=").append(Objects.requireNonNull(object).getId())
                    .append("'; return false\">")
                    .append("<td><a href=\"/do?command=").append(command).append("_detail")
                    .append("&page=").append(page)
                    .append("&order_id=").append(object.getId()).append("\">")
                    .append(object.getId())
                    .append("</a></td>");
            builder.append("<td>").append(object.getDescriptionShort()).append("</td>");
            builder.append("<td>").append(object.getStatus()).append("</td>");
            builder.append("<td>").append(object.getOrderDate()).append("</td>");
            builder.append("<td>").append(object.getExpectedDate()).append("</td>");
            builder.append("<td>").append(object.getUserName()).append("</td>");
            builder.append("<td>").append(object.getEmployeeName()).append("</td>");
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