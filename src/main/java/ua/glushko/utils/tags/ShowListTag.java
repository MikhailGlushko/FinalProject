package ua.glushko.utils.tags;

import ua.glushko.configaration.ConfigurationManager;
import ua.glushko.configaration.MessageManager;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static ua.glushko.services.utils.Authentication.*;
import static ua.glushko.commands.Command.*;

@SuppressWarnings("serial")
abstract class ShowListTag extends TagSupport {
    private List<String> head;
    List<Object> list;

    public void setList(List<Object> list){
        this.list = list;
    }

    public void setHead(List<String> head) {
        this.head = head;
    }

    @Override
    public int doStartTag() throws JspException {
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
            Integer access = Integer.valueOf(pageContext.getRequest().getAttribute(PARAM_ACCESS).toString());
            if((access & C) == C || (access & c) == c ) {
                builder.append("<div class='addbutton' align=\"right\">")
                        .append("<button class='addbutton' type='button' name='button' value='add' onClick=\"window.location.href='/do?command=").append(list.iterator().next().getClass().getSimpleName().toLowerCase()).append("s_add")
                        .append("'\">")
                        .append(MessageManager.getMessage("menu.add", locale))
                        .append("</button>")
                        .append("</div>");
            }
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

    void makeNavigator(StringBuilder builder, Integer pagesCount, Integer rowsCount, Integer page) {
        String command = pageContext.getRequest().getParameter(PARAM_COMMAND);
        int pageCount = list.size()/rowsCount;
        if (list.size()%rowsCount!=0)
            pageCount++;
        int lastPage = pageCount;
        Object attribute = pageContext.getRequest().getAttribute(PARAM_LAST_PAGE);
        if(Objects.nonNull(attribute))
            lastPage = Integer.valueOf(attribute.toString());
        builder.append("<div style=\"text-align: center;\"><ul class=\"pagination\">");
        if(page-1>0) {
            builder.append("<li>").append("<a href=\"/do?command=").append(command).append("&page=").append(1).append("\"><<</a>").append("</li>");
            builder.append("<li>").append("<a href=\"/do?command=").append(command).append("&page=").append(page - 1).append("\"> < </a>").append("</li>");
        }
        for (int i=page-pagesCount; i<page+pagesCount; i++){
            if(i >0 && i-page<pageCount){
                if(i!=page)
                    builder.append("<li>").append("<a href=\"/do?command=").append(command).append("&page=").append(i).append("\">").append(i).append("</a>").append("</li>");
                else
                    builder.append("<li class=\"active\">").append("<a href=\"/do?command=").append(command).append("&page=").append(i).append("\">").append(i).append("</a>").append("</li>");
            }
        }
        if(page<lastPage) {
            builder.append("<li>").append("<a href=\"/do?command=").append(command).append("&page=").append(page + 1).append("\"> > </a>").append("</li>");
            builder.append("<li>").append("<a href=\"/do?command=").append(command).append("&page=").append(lastPage).append("\">>></a>").append("</li>");
        }

        builder.append("</ul></div>");
    }
}