package cn.cixinxc.tinkle.service;


import cn.cixinxc.tinkle.common.model.ServiceProperties;

public interface ServiceProvider {

  void addService(Object service, Class<?> serviceClass, ServiceProperties rpcServiceProperties);

  Object getService(ServiceProperties rpcServiceProperties);

  void publishService(Object service, ServiceProperties rpcServiceProperties);

}
