package cn.edu.cqu.learn.architecture.ibatis.type;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Alias {
    /**
     * Return the alias name.
     *
     * @return the alias name
     */
    String value();
}
