package ua.glushko.utils.tags;

import ua.glushko.model.entity.OrderStatus;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@SuppressWarnings("serial")
public class StatTag extends TagSupport {

    private Map<OrderStatus,Integer> totalOrders;
    private Map<OrderStatus,Integer> newOrders;

    public void setTotalOrders(Map<OrderStatus, Integer> totalOrders) {
        this.totalOrders = totalOrders;
    }

    public void setNewOrders(Map<OrderStatus, Integer> newOrders) {
        this.newOrders = newOrders;
    }

    @Override
    public int doStartTag() throws JspException {
        StringBuilder message = new StringBuilder();
        try {
            message.append("<table class=\"table\">");
            if(Objects.nonNull(totalOrders) || Objects.nonNull(newOrders)){
                message.append("<tr><th>Статус</th><th>Всего</th><th>Новых</th></tr>");
                for (OrderStatus status: OrderStatus.values()) {
                    String totalCount = null;
                    if(Objects.nonNull(totalOrders)){
                        totalCount = Objects.toString(totalOrders.get(status),"0");
                    }
                    String newCount = null;
                    if(Objects.nonNull(newOrders)){
                        newCount = Objects.toString(newOrders.get(status),"0");
                    }
                    if(!totalCount.equals("0") || !newCount.equals("0"))
                        message.append("<tr><td>").append(status).append("</td><td>").append(totalCount).append("</td><td>").append(newCount).append("</td></tr>");
                }

            } else {
                message.append("<div class=\"text-danger\">Нет информации</div>");
            }
            message.append("</table>");
            pageContext.getOut().write(message.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        } finally {
        }
        return SKIP_BODY;
    }
}