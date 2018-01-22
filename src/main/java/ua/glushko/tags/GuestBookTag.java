package ua.glushko.tags;

import ua.glushko.model.entity.GuestBook;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("serial")
public class GuestBookTag extends TagSupport {

    private List<Object> list;
    private int size;
    private int count;

    public void setList(List<Object> list) {
        this.list = list;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int doStartTag() throws JspException {
        if(size==0) size=255;
        if(count==0) count=3;
        try {
            StringBuilder message = new StringBuilder();
            if(Objects.nonNull(list) && list.size()>0){
                Iterator<Object> guestBookIterator = list.iterator();
                while (guestBookIterator.hasNext() && count-->0){
                    Object next = guestBookIterator.next();
                    GuestBook guestBook = null;
                    if(next instanceof GuestBook)
                        guestBook = (GuestBook)next;
                    message.append("<li><strong>").append(guestBook.getActionDate()).append(", ").append(guestBook.getUserName())
                            .append("</strong><br>")
                            .append("<a href=\"#\">").append(guestBook.getDescription()).append("</a>")
                            .append("<div class=text-justify>");
                            if(guestBook.getMemo().length()>size)
                                message.append(guestBook.getMemo().substring(0,size)).append(" ... ");
                            else
                                message.append(guestBook.getMemo());
                            message.append("</div>");
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