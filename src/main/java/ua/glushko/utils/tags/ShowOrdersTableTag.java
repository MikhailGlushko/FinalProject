package ua.glushko.utils.tags;

import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderStatus;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.Command.PARAM_COMMAND;
import static ua.glushko.commands.Command.PARAM_PAGE;

@SuppressWarnings("serial")
public class ShowOrdersTableTag extends ShowListTag {
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
        if (Objects.nonNull(attribute))
            page = (Integer) attribute;
        else
            page = Integer.valueOf(pageContext.getRequest().getParameter(PARAM_PAGE));

        builder.append("<div class=\"row\">");
        for (OrderStatus status : OrderStatus.values()) {
            builder.append("<div class=\"col-lg-2\">");
            Iterator<Object> iterator = list.iterator();
            for (int i = 0; i < rowsCount; i++) {
                Order object;
                if (!iterator.hasNext())
                    continue;
                Object next = iterator.next();
                if (next instanceof Order && ((Order) next).getStatus() == status) {
                    object = (Order) next;
                    builder.append("<div class=\"panel panel-default panel-sm\" onClick=\"window.location.href='")
                            .append("/do?command=").append(command).append("_detail")
                            .append("&page=").append(page)
                            .append("&order_id=").append(Objects.requireNonNull(object).getId())
                            .append("'; return false\">");
                    builder.append("<div class=\"panel panel-heading\">").append(object.getAppliance()).append("</div>");
                    builder.append("<div class=\"panel-body\">");
                    builder.append(object.getDescriptionShort()).append("<br/>");
                    builder.append(object.getUserName()).append("<br/>");
                    builder.append(object.getOrderDate());
                    builder.append("</div></div>");
//                builder.append("<tr onClick=\"window.location.href='")
//                        .append("/do?command=").append(command).append("_detail")
//                        .append("&page=").append(page)
//                        .append("&order_id=").append(Objects.requireNonNull(object).getId())
//                        .append("'; return false\">")
//                        .append("<td><a href=\"/do?command=").append(command).append("_detail")
//                        .append("&page=").append(page)
//                        .append("&order_id=").append(object.getId()).append("\">")
//                        .append(object.getId())
//                        .append("</a></td>");
//                builder.append("<td>").append(object.getDescriptionShort()).append("</td>");
//                builder.append("<td>").append(object.getStatus()).append("</td>");
//                builder.append("<td>").append(object.getOrderDate()).append("</td>");
//                builder.append("<td>").append(object.getExpectedDate()).append("</td>");
//                builder.append("<td>").append(object.getUserName()).append("</td>");
//                builder.append("<td>").append(object.getEmployeeName()).append("</td>");
//                builder.append("</tr>");
                } else i--;
            }
            builder.append("</div>");
        }
        builder.append("</div>");

    }

    public void makeHeader(StringBuilder builder) {
        builder.append( "<div class=\"input-row\"><div class=\"btn btn-default btn-sm col-lg-2\">NEW</div>")
                .append("<div class=\"btn btn-default btn-sm col-lg-2\">INWORK</div>")
                .append("<div class=\"btn btn-default btn-sm col-lg-2\">SUSPEND</div>")
                .append("<div class=\"btn btn-default btn-sm col-lg-2\">COMPLETE</div>")
                .append("<div class=\"btn btn-default btn-sm col-lg-2\">REJECT</div>")
                .append("<div class=\"btn btn-default btn-sm col-lg-2\">CLOSE</div></div>");
    }
}