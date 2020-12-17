package cn.cixinxc.tinkle.common.utils;

import cn.cixinxc.tinkle.common.model.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/18
 */
public class InvokeUtils {
  public static Object invokeTargetMethod(Request request, Object service) {
    if (Objects.isNull(request)
            || Objects.isNull(request.getMethodName())
            || Objects.isNull(request.getParamTypes())) {
      return null;
    }
    try {
      var method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
      return method.invoke(service, request.getParameters());
    } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {

    }
    return null;
  }
}
