package ua.glushko.tags;

import ua.glushko.model.entity.User;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.Command.*;

@SuppressWarnings("serial")
public class ShowUsersListTag extends ShowListTag {
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
            User object = null;
            if (!iterator.hasNext())
                continue;
            Object next = iterator.next();
            if (next instanceof User) {
                object = (User) next;
            }
            builder.append("<tr onClick=\"window.location.href='")
                    .append("/do?command=").append(command).append("_detail&user_id=").append(object.getId())
                    .append("'; return false\">")
                    .append("<td><a href=\"/do?command=").append(command).append("_detail&user_id=").append(object.getId()).append("\">")
                    .append(object.getId())
                    .append("</a></td>");
            builder.append("<td>").append(object.getRole()).append("</td>");
            builder.append("<td>").append(object.getName()).append("</td>");
            builder.append("<td>").append(object.getLogin()).append("</td>");
            builder.append("<td>").append(object.getPassword()).append("</td>");
            builder.append("<td>").append(object.getEmail()).append("</td>");
            builder.append("<td>").append(object.getPhone()).append("</td>");
            builder.append("<td>").append(object.getStatus()).append("</td>");
            builder.append("<td>").append(object.getLastLogin()).append("</td>");
            builder.append("</tr>");
        }
    }
}