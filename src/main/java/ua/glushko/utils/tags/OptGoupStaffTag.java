package ua.glushko.utils.tags;

import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;

import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
/**
 * Generate Options input tag with group selector for Users
 * @version 1.0
 * @author Mikhail Glushko
 */

@SuppressWarnings("serial")
public class OptGoupStaffTag extends TagSupport {
    private List<User> list;
    private int value;

    public void setList(List<User> list){
        this.list = list;
    }

    public void setValue(int value){
        this.value=value;
    }

    @SuppressWarnings("SameReturnValue")
    @Override
    public int doStartTag() {
        StringBuilder builder = new StringBuilder();
        for (UserRole userRole: UserRole.values()){
            builder.append("<optgroup label=\"").append(userRole.name()).append("\">");
            for (User user:list) {
                if (user.getRole()==userRole){
                    builder.append("<option value=\"").append(user.getId()).append("\"");
                    if(user.getId()==value)
                        builder.append(" selected ");
                    builder.append(">").append(escapeHtml(user.getName())).append("</option>");
                }
            }
            builder.append("</optiongroup>");
        }
        //noinspection EmptyCatchBlock
        try {
            pageContext.getOut().write(builder.toString());
        } catch (IOException e) {
        }
        return SKIP_BODY;
    }
}