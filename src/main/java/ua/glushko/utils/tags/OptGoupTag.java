package ua.glushko.utils.tags;

import ua.glushko.model.entity.RepairService;

import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Generate Options input tag with group selector for RepairServices
 * @version 1.0
 * @author Mikhail Glushko
 */

@SuppressWarnings("ALL")
public class OptGoupTag extends TagSupport {
    private List<Object> list;
    private int value;

    public void setList(List<Object> list){
        this.list = list;
    }

    public void setValue(int value){
        this.value=value;
    }

    @SuppressWarnings({"SameReturnValue", "unused"})
    @Override
    public int doStartTag() {
        StringBuilder builder = new StringBuilder();
        if(Objects.nonNull(list))
        for (Object o:list) {
            if(o instanceof RepairService){
                RepairService r  = (RepairService) o;
                if (r.getParent()==0){
                    builder.append("<optgroup label=\"").append(r.getNameRu()).append("\">");
                    for (Object l: list) {
                        if(l instanceof RepairService){
                            RepairService s = (RepairService) l;
                            if(s.getParent()==r.getId()){
                                builder.append("<option value=\"").append(s.getId()).append("\"");
                                if(s.getId()==value)
                                    builder.append(" selected ");
                                builder.append(">").append(s.getNameRu()).append("</option>");
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