package ua.glushko.utils.tags;

import ua.glushko.model.entity.OrderStats;
import ua.glushko.model.entity.OrderStatus;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Generate Orders statistics as table
 * @version 1.0
 * @author Mikhail Glushko
 */

@SuppressWarnings("serial")
public class StatTag extends TagSupport {

    private Map<OrderStatus,Map<OrderStats,Integer>> totalOrders;

    public void setTotalOrders(Map<OrderStatus, Map<OrderStats, Integer>> totalOrders) {
        this.totalOrders = totalOrders;
    }

    @SuppressWarnings("SameReturnValue")
    @Override
    public int doStartTag() throws JspException {
        StringBuilder message = new StringBuilder();
        try {
            message.append("<table class=\"table\">");
            if(Objects.nonNull(totalOrders)){
                message.append("<tr><th>Статус</th><th>Всего</th><th>Поступило сегодня</th><th>Движение сегодня</th><th>Мои</th><th>К исполнению</th></tr>");
                for (OrderStatus status: OrderStatus.values()) {
                    Map<OrderStats, Integer> orderStatsMap = totalOrders.get(status);
                    if(Objects.nonNull(orderStatsMap)) {
                        String countAll = Objects.toString(orderStatsMap.get(OrderStats.STATUS), "0");
                        String countNew = Objects.toString(orderStatsMap.get(OrderStats.NEW), "0");
                        String countToday = Objects.toString(orderStatsMap.get(OrderStats.TODAY), "0");
                        String countCurrentUser = Objects.toString(orderStatsMap.get(OrderStats.CURRENT_USER), "0");
                        String countExecution = Objects.toString(orderStatsMap.get(OrderStats.EXECUTION), "0");
                        if (!countAll.equals("0"))
                            message.append("<tr><td>").append(status)
                                    .append("</td><td>").append(countAll)
                                    .append("</td><td>").append(countNew)
                                    .append("</td><td>").append(countToday)
                                    .append("</td><td>").append(countCurrentUser)
                                    .append("</td><td>").append(countExecution)
                                    .append("</td></tr>");
                    }
                }

            } else {
                message.append("<div class=\"text-danger\">Нет информации</div>");
            }
            message.append("</table>");
            pageContext.getOut().write(message.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}