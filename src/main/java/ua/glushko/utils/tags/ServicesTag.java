package ua.glushko.utils.tags;

import ua.glushko.model.entity.RepairService;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Generate List of RepairServices
 * @version 1.0
 * @author Mikhail Glushko
 */

@SuppressWarnings({"serial", "unused"})
public class ServicesTag extends TagSupport {

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
                    RepairService item = null;
                    if (next instanceof RepairService)
                        item = (RepairService) next;
                    if (Objects.nonNull(item) && item.getParent() == 0) {
                        message.append("<div class=\"panel-heading\" style=\"cursor: pointer\">\n")
                                .append("   <H4 class=\"panel-title\" data-toggle=\"collapse\" data-parent=\"#collapse-group\"\n")
                                .append("   href=\"#el").append(item.getId()).append("\">").append(item.getNameRu()).append("</H4>\n")
                                .append("</div>")

                                .append("<div id=\"el").append(item.getId()).append("\" class=\"panel-collapse collapse\">\n")
                                .append("   <div class=\"panel-body\">\n")
                                .append("       <ul>\n");
                        for (Object subItem : list) {
                            if (subItem instanceof RepairService && ((RepairService) subItem).getParent() == item.getId()) {
                                message.append("<li class=\"btn-link\">").append(((RepairService) subItem).getNameRu()).append("</li>\n");
                            }
                        }
                        message.append("        </ul>\n")
                                .append("   </div>\n")
                                .append("</div>\n");
                    }

                }
            }
            pageContext.getOut().write(message.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage());
        }
        return SKIP_BODY;
    }
}