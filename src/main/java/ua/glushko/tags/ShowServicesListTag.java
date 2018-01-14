package ua.glushko.tags;

import ua.glushko.model.entity.RepairService;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.Command.PARAM_NAME_COMMAND;

@SuppressWarnings("serial")
public class ShowServicesListTag extends ShowListTag {
    public void makeBody(List<Object> list, StringBuilder builder, Integer rowsCount) {
        // build bode if table
        String command = null;
        Object attribute = pageContext.getRequest().getAttribute(PARAM_NAME_COMMAND);
        if (Objects.nonNull(attribute))
            command = (String) attribute;
        else
            command = pageContext.getRequest().getParameter(PARAM_NAME_COMMAND);
        Iterator<Object> iterator = list.iterator();
        for (int i = 0; i < rowsCount; i++) {
            RepairService object = null;
            if (!iterator.hasNext())
                continue;
            Object next = iterator.next();
            if (next instanceof RepairService) {
                object = (RepairService) next;
            }
            builder.append("<tr onClick=\"window.location.href='")
                    .append("/do?command=").append(command).append("_detail&service_id=").append(object.getId())
                    .append("'; return false\">")
                    .append("<td><a href=\"/do?command=").append(command).append("_detail&service_id=").append(object.getId()).append("\">")
                    .append(object.getId())
                    .append("</a></td>");
            builder.append("<td>").append(object.getName()).append("</td>");
            builder.append("<td>").append(object.getNameRu()).append("</td>");
            builder.append("<td>").append(object.getParent()).append("</td>");
            builder.append("</tr>");
        }
    }
}