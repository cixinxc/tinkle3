package cn.cixinxc.tinkle.service;


import cn.cixinxc.tinkle.common.model.ServiceProperties;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ServiceProviderImpl implements ServiceProvider {

  private static final Map<String, Object> registeredServiceMap;


  static {
    registeredServiceMap = new ConcurrentHashMap<>();
  }

  public ServiceProviderImpl() {
  }

  @Override
  public void addService(Object service, Class<?> serviceClass, ServiceProperties rpcServiceProperties) {
    String rpcServiceName = rpcServiceProperties.getServiceName();
    if (registeredServiceMap.keySet().contains(rpcServiceName)) {
      return;
    }
    registeredServiceMap.put(rpcServiceName, service);
  }

  @Override
  public Object getService(ServiceProperties rpcServiceProperties) {
    Object service = registeredServiceMap.get(rpcServiceProperties.getServiceName());
    if (null == service) {
    }
    return service;
  }

  @Override
  public void publishService(Object service, ServiceProperties rpcServiceProperties) {

  }

}
