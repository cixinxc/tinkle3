package cn.cixinxc.tinkle.common.utils;

import cn.cixinxc.tinkle.common.model.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
  public static Object invokeTargetMethod(Object target, RpcRequest rpcRequest) {
    if (Objects.isNull(rpcRequest)
            || Objects.isNull(rpcRequest.getMethodName())
            || Objects.isNull(rpcRequest.getParamTypes())) {
      return null;
    }
    try {
      Method method = target.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
      return method.invoke(target, rpcRequest.getParameters());
    } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
      logger.error("invoke target method failed. target:{}, request:{}.", target, rpcRequest.toString(), e);
    }
    return null;
  }
}
