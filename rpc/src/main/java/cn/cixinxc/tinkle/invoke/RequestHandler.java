package cn.cixinxc.tinkle.invoke;

import cn.cixinxc.tinkle.common.model.Request;
import cn.cixinxc.tinkle.service.ServiceProvider;
import cn.cixinxc.tinkle.service.ServiceProviderImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/27
 */
public class RequestHandler {
  private final static ServiceProvider serviceProvider = ServiceProviderImpl.getInstance();


  /**
   * Processing rpcRequest: call the corresponding method, and then return the method
   */
  public static Object handle(Request request) {
    Object service = serviceProvider.getService(request.getServiceProperties());
    return invokeTargetMethod(request, service);
  }

  /**
   * get method execution results
   *
   * @param request client request
   * @param service service object
   * @return the result of the target method execution
   */
  private static Object invokeTargetMethod(Request request, Object service) {
    Object result = null;
    try {
      Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
      result = method.invoke(service, request.getParameters());
    } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
      return result;
    }
    return result;
  }
}
