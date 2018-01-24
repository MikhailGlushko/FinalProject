package ua.glushko.utils.tags;

import ua.glushko.commands.impl.admin.orders.OrdersCommandHelper;
import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;
import ua.glushko.model.entity.Order;
import ua.glushko.model.entity.OrderHistory;

import javax.servlet.jsp.JspException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.Command.*;
import static ua.glushko.commands.impl.admin.orders.OrdersCommandHelper.PARAM_ORDER_HISTORY_PAGE;

@SuppressWarnings("serial")
public class ShowOrdersHistoryListTag extends ShowListTag {

    public int doStartTag() throws JspException {
        try {
            StringBuilder builder = new StringBuilder();

            String property;
            Integer pagesCount;
            Integer rowsCount;
            Integer page;
            try {
                property = ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_PAGES_COUNT);
                pagesCount = Integer.valueOf(property);
                property = ConfigurationManager.getProperty(PROPERTY_NAME_BROWSER_ROWS_COUNT);
                rowsCount = Integer.valueOf(property);
            } catch (NullPointerException e) {
                    pagesCount = 5;
                    rowsCount = 5;
            }

            if (pageContext.getRequest().getParameter(PARAM_ORDER_HISTORY_PAGE) != null)
                page = Integer.valueOf(pageContext.getRequest().getParameter(PARAM_ORDER_HISTORY_PAGE));
            else
                page = 1;

            // build head of table
            builder.append("<table class=\"browser\">");
            makeHeader(builder);
            builder.append("<tbody>");
            makeBody(list,builder, rowsCount);
            builder.append("</tbody>");
            builder.append("</table><br/>");

            makeNavigator(builder, pagesCount, rowsCount, page);
            pageContext.getOut().write(builder.toString());
        } catch (IOException | NumberFormatException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    protected void makeBody(List<Object> list, StringBuilder builder, Integer rowsCount) {
        // build bode if table
        Iterator<Object> iterator = list.iterator();
        for (int i = 0; i < rowsCount; i++) {
            OrderHistory object = null;
            if (!iterator.hasNext())
                continue;
            Object next = iterator.next();
            if (next instanceof OrderHistory) {
                object = (OrderHistory) next;
            }
            builder.append("<tr >")
                    .append("<td>").append(Objects.requireNonNull(object).getId()).append("</td>");
            builder.append("<td>").append(object.getAction()).append("</td>");
            builder.append("<td>").append(object.getDescription()).append("</td>");
            builder.append("<td>").append(object.getActionDate()).append("</td>");
            builder.append("<td>").append(object.getUserName()).append("</td>");
            builder.append("</tr>");
        }
    }

    void makeHeader(StringBuilder builder) {
            builder.append("<thead><tr>");
            builder.append("<th>ID</th>").append("<th>ACTION</th>").append("<th>DESCRIPTION</th>").append("<th>ACTION DATE</th>").append("<th>MAKER</th>");
            builder.append("</tr></thead>");
    }

    @Override
    void makeNavigator(StringBuilder builder, Integer pagesCount, Integer rowsCount, Integer page) {
        String currentPage = pageContext.getRequest().getParameter(PARAM_PAGE);
        if (currentPage==null || currentPage.isEmpty())
            currentPage = "1";
        Object orderObg = pageContext.getRequest().getAttribute(OrdersCommandHelper.PARAM_ORDER);
        Integer id = null;
        if(orderObg!=null && orderObg instanceof Order) {
            id = ((Order) orderObg).getId();
        }
        // build navigator
        builder.append("<table class=\"navigator\"><tr>").append("<td>").append(MessageManager.getMessage("browser.pages")).append("</td>");
        for (int i=page-pagesCount; i<page+pagesCount; i++){
            int pageCount = list.size()/rowsCount;
            if (list.size()%rowsCount!=0)
                pageCount++;
            if(i >0 && i-page<pageCount){
                if(page!=i){
                    String command = pageContext.getRequest().getParameter(PARAM_COMMAND);
                    builder.append("<td>").append("<a href=\"/do?command=").append(command).append("&order_id=").append(id).append("&page=").append(currentPage)
                            .append("&history_page=").append(i).append("\">").append(i).append("</a>").append("</td>");
                } else {
                    builder.append("<td>").append(" [ ").append(i).append(" ] ").append("</td>");
                }
            }
        }
        builder.append("</tr></table>");
    }
}