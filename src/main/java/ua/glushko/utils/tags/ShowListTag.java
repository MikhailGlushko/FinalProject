package ua.glushko.utils.tags;

import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static ua.glushko.commands.CommandFactory.PARAM_SERVLET_PATH;
import static ua.glushko.commands.utils.Authentication.*;
import static ua.glushko.commands.Command.*;

/**
 * Template for draw tables
 * @version 1.0
 * @author Mikhail Glushko
 */

@SuppressWarnings("serial")
abstract class ShowListTag extends TagSupport {
    private List<String> head;
    private List<Object> list;

    @SuppressWarnings("unused")
    public void setList(List<Object> list){
        this.list = list;
    }

    @SuppressWarnings("unused")
    public void setHead(List<String> head) {
        this.head = head;
    }

    @Override
    public int doStartTag() {
        try {
            HttpSession session = pageContext.getSession();
            String locale = (String) session.getAttribute(PARAM_LOCALE);
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

            if (pageContext.getRequest().getParameter(PARAM_PAGE) != null
                    && !Objects.equals(pageContext.getRequest().getParameter(PARAM_PAGE), "")
                    && !pageContext.getRequest().getParameter(PARAM_PAGE).equals("null"))
                page = Integer.valueOf(pageContext.getRequest().getParameter(PARAM_PAGE));
            else
                page = 1;

            // build head of table
            Integer access = 0;
            if(Objects.nonNull(pageContext.getRequest().getAttribute(PARAM_ACCESS)))
                access = Integer.valueOf(pageContext.getRequest().getAttribute(PARAM_ACCESS).toString());
            if((access & C) == C || (access & c) == c ) {
                builder.append("<div class='addbutton' align=\"right\">")
                        .append("<button class='addbutton' type='button' name='button' value='add' onClick=\"window.location.href='")
                        .append(PARAM_SERVLET_PATH)
                        .append("?command=").append(list.iterator().next().getClass().getSimpleName().toLowerCase()).append("s_add")
                        .append("'\">")
                        .append(MessageManager.getMessage("menu.add", locale))
                        .append("</button>")
                        .append("</div>");
            }
            builder.append("<table class=\"browser\">");
            makeHeader(builder);
            makeBody(list,builder, rowsCount);
            builder.append("</table><br/>");

            makeNavigator(builder, pagesCount, rowsCount, page);
            pageContext.getOut().write(builder.toString());
        } catch (IOException | NumberFormatException e) {
            //throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }

    void makeHeader(StringBuilder builder) {
        Iterator<String> headIterator = head.iterator();
        if (headIterator.hasNext()) {
            builder.append("<thead><tr>");
            while (headIterator.hasNext()) {
                builder.append("<th>").append(headIterator.next()).append("</th>");
            }
            builder.append("</tr></thead>");
        }
    }

    protected abstract void makeBody(List<Object> list, StringBuilder builder, Integer rowsCount);

    private void makeNavigator(StringBuilder builder, Integer pagesCount, Integer rowsCount, Integer page) {
        String command;
        Object attr = pageContext.getRequest().getAttribute(PARAM_COMMAND);
        if(Objects.isNull(attr))
            command = pageContext.getRequest().getParameter(PARAM_COMMAND);
        else {
            command = (String) pageContext.getRequest().getAttribute(PARAM_COMMAND);
        }
        int pageCount = list.size()/rowsCount;
        if (list.size()%rowsCount!=0)
            pageCount++;
        int lastPage = pageCount;
        Object attribute = pageContext.getRequest().getAttribute(PARAM_LAST_PAGE);
        if(Objects.nonNull(attribute))
            lastPage = Integer.valueOf(attribute.toString());
        builder.append("<div style=\"text-align: center;\"><ul class=\"pagination\">");
        if(page-1>0) {
            builder.append("<li>").append("<a href=\"")
                    .append(PARAM_SERVLET_PATH)
                    .append("?command=").append(command).append("&page=").append(1).append("\"><<</a>").append("</li>");
            builder.append("<li>").append("<a href=\"")
                    .append(PARAM_SERVLET_PATH)
                    .append("?command=").append(command).append("&page=").append(page - 1).append("\"> < </a>").append("</li>");
        }
        for (int i=page-pagesCount; i<page+pagesCount; i++){
            if(i >0 && i-page<pageCount){
                if(i!=page)
                    builder.append("<li>").append("<a href=\"")
                            .append(PARAM_SERVLET_PATH)
                            .append("?command=").append(command).append("&page=").append(i).append("\">").append(i).append("</a>").append("</li>");
                else
                    builder.append("<li class=\"active\">").append("<a href=\"")
                            .append(PARAM_SERVLET_PATH)
                            .append("?command=").append(command).append("&page=").append(i).append("\">").append(i).append("</a>").append("</li>");
            }
        }
        if(page<lastPage) {
            builder.append("<li>").append("<a href=\"")
                    .append(PARAM_SERVLET_PATH)
                    .append("?command=").append(command).append("&page=").append(page + 1).append("\"> > </a>").append("</li>");
            builder.append("<li>").append("<a href=\"")
                    .append(PARAM_SERVLET_PATH)
                    .append("?command=").append(command).append("&page=").append(lastPage).append("\">>></a>").append("</li>");
        }

        builder.append("</ul></div>");
    }
}