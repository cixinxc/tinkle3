package cn.cixinxc.tinkle.common.utils;

import cn.cixinxc.tinkle.common.model.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/18
 */
public class InvokeUtils {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  /**
   * reflect invoke target method
   *
   * @param target target
   * @param
   */
  public static Object invokeTargetMethod(Object target, Request request) {
    if (Objects.isNull(request)
            || Objects.isNull(request.getMethodName())
            || Objects.isNull(request.getParamTypes())) {
      return null;
    }
    try {
      var method = target.getClass().getMethod(request.getMethodName(), request.getParamTypes());
      return method.invoke(target, request.getParameters());
    } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
      logger.error("invoke target method failed. target:{}, request:{}.", target, request.toString(), e);
    }
    return null;
  }
}
