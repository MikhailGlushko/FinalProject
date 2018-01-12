package ua.glushko.commands;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class CommandHelper {

    private final ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<>();
    private final ThreadLocal<HttpServletRequest> request = new ThreadLocal<>();

    public CommandHelper(HttpServletRequest request) {
        this.request.set(request);
        collectParameters();
    }

    public <T> T get(String key){
        try{
            Map<String, Object> stringObjectMap = threadLocal.get();
            Object[] object = (Object[]) stringObjectMap.get(key);
            if(object.length==3){
                Object obj = object[1];
                return (T)obj;
            }
        } catch (Exception e){}
        return null;
    }

    public String getString(String key){
        try {
            Map<String, Object> stringObjectMap = threadLocal.get();
            Object[] object = (Object[]) stringObjectMap.get(key);
            if (object.length == 2) {
                String type = (String) object[0];
                Object value = object[1];
                return value.toString();
            }
        } catch (Exception e){
        }
        return null;
    }

    public Integer getInt(String key){
        try {
            Map<String, Object> stringObjectMap = threadLocal.get();
            Object[] object = (Object[]) stringObjectMap.get(key);
            if (object.length == 2) {
                String type = (String) object[0];
                Object value = object[1];
                return Integer.valueOf(value.toString());
            }
        } catch (Exception e){
        }
        return null;
    }

    public Object getObj(String key){
        Map<String, Object> stringObjectMap = threadLocal.get();
        Object[] object = (Object[]) stringObjectMap.get(key);
        try{
            if(object.length==2)
                return object[1];
        } catch (Exception e){
        }
        return null;
    }

    public void collectParameters(){
        Map<String, Object> result = new HashMap<>();
        // collect parameters from request
        Enumeration<String> attributeNames = request.get().getParameterNames();
        while (attributeNames.hasMoreElements()){
            String elementName = attributeNames.nextElement();
            String attributeValue = request.get().getParameter(elementName);
            String attributeType = attributeValue.getClass().getName();
            Object[] attribute = new Object[] {attributeType, attributeValue};
            result.put(elementName,attribute);
        }
        // collect attributes from request
        attributeNames = request.get().getAttributeNames();
        while (attributeNames.hasMoreElements()){
            String elementName = attributeNames.nextElement();
            Object attributeValue = request.get().getAttribute(elementName);
            String attributeType = attributeValue.getClass().getName();
            Object[] attribute = new Object[] {attributeType, attributeValue};
            result.put(elementName,attribute);
        }
        // collect attributes from session
        attributeNames = request.get().getSession().getAttributeNames();
        while (attributeNames.hasMoreElements()){
            String elementName = attributeNames.nextElement();
            Object attributeValue = request.get().getSession().getAttribute(elementName);
            String attributeType = attributeValue.getClass().getName();
            Object[] attribute = new Object[] {attributeType, attributeValue};
            result.put(elementName,attribute);
        }
        threadLocal.set(result);
    }
}
