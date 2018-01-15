package ua.glushko.tags;

import ua.glushko.model.entity.RepairService;
import ua.glushko.model.entity.User;
import ua.glushko.model.entity.UserRole;

import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;

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
                    builder.append(">").append(user.getName()).append("</option>");
                }
            }
            builder.append("</optiongroup>");
        }
        try {
            pageContext.getOut().write(builder.toString());
        } catch (IOException e) {
        }
        return SKIP_BODY;
    }
}