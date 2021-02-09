package cn.cixinxc.tinkle.invoke;

import cn.cixinxc.tinkle.common.model.Request;
import cn.cixinxc.tinkle.service.provider.ServiceProvider;
import cn.cixinxc.tinkle.service.provider.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/27
 */
public class RequestHandler {

  private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

  private final static ServiceProvider serviceProvider = ServiceProviderImpl.getInstance();

  public static Object handle(Request request) {
    var service = serviceProvider.getService(request.getServiceProperties());
    if (service == null) {
      return null;
    }
    try {
      Method method = service.getClass().getMethod(request.getMethodName(), request.getParamTypes());
      return method.invoke(service, request.getParameters());
    } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
      logger.error("handle request error. request:{}.", request, e);
    }
    return null;
  }
}
