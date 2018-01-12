package ua.glushko.model.reflection;

import java.lang.annotation.*;

/**
 * Аннотация для указания что данное поле необходимо сохнанить в базу данных и какому полю в таблице оно соответствует.
 *  По умолчаниею, если не задан параметр name - берется название поля в базе данных такое же как и название поля в классе.
 *  Если поле не помечено данной аннотацией - оно не будет сохранятся в базу данных и в него не будут вносится данные
 *  при получение из базы данных
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
    String name() default "";
}

