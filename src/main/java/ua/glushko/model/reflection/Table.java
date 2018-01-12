package ua.glushko.model.reflection;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
/** Аннотация для указания какой таблице хранится сущность*/
public @interface Table {
    String name() default "";
}
