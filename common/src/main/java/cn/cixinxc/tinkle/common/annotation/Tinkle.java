package cn.cixinxc.tinkle.common.annotation;


import java.lang.annotation.*;

/**
 * marked on the implementation class of service
 *
 * @author Cui Xinxin
 * @createTime 2020/12/17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface Tinkle {

  String version() default "";

  String group() default "";

}
