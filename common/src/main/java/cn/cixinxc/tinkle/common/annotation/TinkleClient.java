package cn.cixinxc.tinkle.common.annotation;


import java.lang.annotation.*;

/**
 * marked on the use service
 *
 * @author Cui Xinxin
 * @createTime 2020/12/17
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface TinkleClient {

  String version() default "0";

  String group() default "DEFAULT";

}
