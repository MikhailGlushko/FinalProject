package ua.glushko.tags;

import ua.glushko.model.entity.RepairService;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class OptGoupTag extends TagSupport {
    public List<Object> list;

    public void setList(List<Object> list){
        this.list = list;
    }

    @Override
    public int doStartTag() throws JspException {
        StringBuilder builder = new StringBuilder();
        for (Object o:list) {
            if(o instanceof RepairService){
                RepairService r  = (RepairService) o;
                if (r.getParent()==0){
                    builder.append("<optgroup label=\"").append(r.getNameRu()).append("\">");
                    for (Object l: list) {
                        if(l instanceof RepairService){
                            RepairService s = (RepairService) l;
                            if(s.getParent()==r.getId()){
                                builder.append("<option value=\"").append(s.getId()).append("\">").append(s.getNameRu()).append("</option>");
                            }
                        }
                    }
                    builder.append("</optiongroup>");
                }
            }
        }
        try {
            pageContext.getOut().write(builder.toString());
        } catch (IOException e) {
        }
        return SKIP_BODY;
    }
}