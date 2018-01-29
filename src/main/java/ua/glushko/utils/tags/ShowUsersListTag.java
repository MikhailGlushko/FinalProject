package ua.glushko.utils.tags;

import ua.glushko.model.entity.User;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.Command.*;
import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
@SuppressWarnings("serial")
public class ShowUsersListTag extends ShowListTag {
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
        else
            page = Integer.valueOf(pageContext.getRequest().getParameter(PARAM_PAGE));

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
                    .append(PARAM_SERVLET_PATH)
                    .append("?command=").append(command).append("_detail")
                    .append("&page=").append(page)
                    .append("&user_id=").append(Objects.requireNonNull(object).getId())
                    .append("'; return false\">")
                    .append("<td><a href=\"")
                    .append(PARAM_SERVLET_PATH)
                    .append("?command=").append(command).append("_detail")
                    .append("&page=").append(page)
                    .append("&user_id=").append(object.getId()).append("\">")
                    .append(object.getId())
                    .append("</a></td>");
            builder.append("<td>").append(object.getRole()).append("</td>");
            builder.append("<td>").append(escapeHtml(object.getName())).append("</td>");
            builder.append("<td>").append(escapeHtml(object.getEmail())).append("</td>");
            builder.append("<td>").append(escapeHtml(object.getPhone())).append("</td>");
            builder.append("</tr>");
        }
    }

    void makeHeader(StringBuilder builder) {
            builder.append("<thead><tr>");
            builder.append("<th>ID</th><th>ROLE</th><th>NAME</th><th>EMAIL</th><th>PHONE</th>");
            builder.append("</tr></thead>");
    }
}