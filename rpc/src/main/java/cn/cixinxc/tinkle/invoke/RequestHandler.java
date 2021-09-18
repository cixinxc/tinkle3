package cn.cixinxc.tinkle.invoke;

import cn.cixinxc.tinkle.common.model.RpcRequest;
import cn.cixinxc.tinkle.service.provider.ServiceProvider;
import cn.cixinxc.tinkle.service.provider.ServiceProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/27
 */
public class RequestHandler {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private final static ServiceProvider serviceProvider = ServiceProviderImpl.getInstance();

  public static Object handle(RpcRequest rpcRequest) {
    Object service = serviceProvider.getService(rpcRequest.getServiceProperties());
    if (service == null) {
      return null;
    }
    try {
      Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
      return method.invoke(service, rpcRequest.getParameters());
    } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
      logger.error("handle request error. request:{}.", rpcRequest, e);
    }
    return null;
  }
}
