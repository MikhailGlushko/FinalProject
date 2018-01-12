package ua.glushko.model.reflection;

import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class ReflectDAOUtils {

    private static final Logger logger = Logger.getLogger(ReflectDAOUtils.class.getSimpleName());

    /**
     * На основании оннотации @Table получить из класса данные а таблице, где должны хранится сущности
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        String tableName = null;
        Table annotation = clazz.getAnnotation(Table.class);
        Class<? extends Annotation> aClass = annotation.annotationType();
        try {
            Method method = aClass.getMethod("name");
            tableName = (String) method.invoke(annotation);
        } catch (NoSuchMethodException e) {
            logger.error(e);
        } catch (IllegalAccessException e) {
            logger.error(e);
        } catch (InvocationTargetException e) {
            logger.error(e);
        }
        return tableName;
    }

    /**
     * На основании аннотации @Field получить список мапинга полей сущности в поля таблицы.
     * @param clazz
     * @return
     */
    private static Map<String,String> getFieldsMapping(Class<?> clazz) {
        Map<String,String> mapping = new LinkedHashMap<>();
        java.lang.reflect.Field[] fields = clazz.getDeclaredFields();
        for (java.lang.reflect.Field field: fields) {
            Field annotation = field.getAnnotation(Field.class);
            if (annotation!=null){
                Class<? extends Annotation> aClass = annotation.annotationType();
                try {
                    Method method = aClass.getMethod("name");
                    String fieldName = (String) method.invoke(annotation);
                    if (fieldName.isEmpty())
                        fieldName = field.getName();
                    mapping.put(field.getName(),fieldName);
                } catch (NoSuchMethodException e) {
                    logger.error(e);
                } catch (IllegalAccessException e) {
                    logger.error(e);
                } catch (InvocationTargetException e) {
                    logger.error(e);
                }
            }
        }
        return mapping;
    }

    /** настроить стайтмент для запроса создания записи*/
    public static void prepareStatementForCreate(PreparedStatement statement, Object object) throws SQLException {
        Object[] objects = getFieldsMapping(object.getClass()).keySet().toArray();
        int index=1;
        for (Object obj : objects) {
            String str = (String) obj;
            // для всех ранее найденных полей кроме id
            if ("id".equals(str))
                continue;
            try {
                String methodName = "get" + Character.toUpperCase(str.charAt(0)) + str.substring(1);
                Method method = object.getClass().getDeclaredMethod(methodName);
                // получаем значение поля через getter
                Object invokedValue = method.invoke(object);
                // взависимости от типа результата вносим даннык в стейтмент
                if (invokedValue instanceof String)
                    statement.setString(index++, (String) invokedValue);
                else if (invokedValue instanceof Integer)
                    statement.setInt(index++, (Integer) invokedValue);
                else if (invokedValue instanceof Boolean)
                    statement.setBoolean(index++, (Boolean) invokedValue);
                else if (invokedValue instanceof Date)
                    statement.setDate(index++, (Date) invokedValue);
                else
                    statement.setString(index++, invokedValue.toString());
            } catch (NoSuchMethodException e) {
                logger.error(e);
            } catch (IllegalAccessException e) {
                logger.error(e);
            } catch (InvocationTargetException e) {
                logger.error(e);
            }
        }
    }

    /**
     *  Формирует список параметров для запроса на основании аннотаций
     * @param object
     * @return
     */
    public static String getFieldList(Class<?> object){
        Object[] objects = getFieldsMapping(object).values().toArray();
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<objects.length-1; i++) {
            Object obj = objects[i];
            String str = (String) obj;
            if ("id".equals(str))
                continue;
            sb.append(str).append(",");
        }
        if (objects.length>0){
            Object obj = objects[objects.length-1];
            String str = (String) obj;
            sb.append(str);
        }
        return sb.toString();
    }

    public static List parseResultSet(ResultSet resultSet, Class<?> object) throws SQLException {

        Object[] objects = getFieldsMapping(object).keySet().toArray();
        List list = new ArrayList<>();
        while (resultSet.next()){
            Object item = null;
            try {
                item = object.newInstance();
            } catch (InstantiationException e) {
                logger.error(e);
            } catch (IllegalAccessException e) {
                logger.error(e);
            }

            for (Object obj : objects) {
                String objectFieldName = (String) obj;
                String dbFieldName = getFieldsMapping(object).get(objectFieldName);
                String methodName = "set" + Character.toUpperCase(objectFieldName.charAt(0)) + objectFieldName.substring(1);
                Method[] methods = item.getClass().getDeclaredMethods();
                for (Method method : methods) {
                    String name = method.getName();
                    if (name != null && name.contains(methodName)) {
                        Class[] parameters = method.getParameterTypes();
                        if (parameters.length != 1)
                            continue;
                        String parameter = parameters[0].getSimpleName();
                        if (parameter != null) {
                            try {
                                switch (parameter) {
                                    case "Integer": {
                                        Integer value = resultSet.getInt(dbFieldName);
                                        Object[] args = {value};
                                        method.invoke(item, args);
                                        break;
                                    }
                                    case "Date": {
                                        Date value = resultSet.getDate(dbFieldName);
                                        Object[] args = {value};
                                        method.invoke(item, args);
                                        break;
                                    }
                                    case "Boolean": {
                                        Date value = resultSet.getDate(dbFieldName);
                                        Object[] args = {value};
                                        method.invoke(item, args);
                                        break;
                                    }
                                    case "String": {
                                        String value = resultSet.getString(dbFieldName);
                                        Object[] args = {value};
                                        method.invoke(item, args);
                                        break;
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                logger.error(e);
                            } catch (InvocationTargetException e) {
                                logger.error(e);
                            }
                        }
                    }
                }
            }
            list.add(item);
        }
        return list;
    }
}
