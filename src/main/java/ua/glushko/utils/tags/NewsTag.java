package ua.glushko.utils.tags;

import ua.glushko.model.entity.News;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

/**
 * Print News data on the page
 * @version 1.0
 * @author Mikhail Glushko
 */

@SuppressWarnings("ALL")
public class NewsTag extends TagSupport {

    private List<Object> list;

    public void setList(List<Object> list) {
        this.list = list;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            StringBuilder message = new StringBuilder();
            if(Objects.nonNull(list) && list.size()>0){
                for (Object next : list) {
                    News news = null;
                    if (next instanceof News)
                        news = (News) next;
                    message.append("<li><strong>").append(Objects.requireNonNull(news).getActionDate()).append("</strong>")
                            .append("<a href=\"#\">").append(escapeHtml(news.getDescription())).append("</a>")
                            .append("<div class=text-justify>")
                            .append(escapeHtml(news.getMemo()).substring(0, 255)).append(" ... ")
                            .append("</div>");
                    message.append("</li>");
                }
            } else {
                message.append("<div class=\"text-danger\">Нет информации</div>");
            }
            pageContext.getOut().write(message.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}