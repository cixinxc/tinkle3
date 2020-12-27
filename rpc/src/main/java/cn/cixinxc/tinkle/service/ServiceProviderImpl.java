package cn.cixinxc.tinkle.service;


import cn.cixinxc.tinkle.common.annotation.Tinkle;
import cn.cixinxc.tinkle.common.instance.CommonConstants;
import cn.cixinxc.tinkle.common.model.ServiceProperties;
import cn.cixinxc.tinkle.register.RegisterService;
import cn.cixinxc.tinkle.register.ZookeeperRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ServiceProviderImpl implements ServiceProvider {

  private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

  private static final int SERVICE_MAP_DEFAULT_CAPACITY = 4096;
  private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>(SERVICE_MAP_DEFAULT_CAPACITY);
  private static final Set<String> serviceNameSet = new HashSet<>(SERVICE_MAP_DEFAULT_CAPACITY);

  private static final ServiceProvider serviceProvider = new ServiceProviderImpl();

  private static final RegisterService registerService = new ZookeeperRegisterService();

  private ServiceProviderImpl() {
  }

  public static ServiceProvider getInstance() {
    return serviceProvider;
  }

  @Override
  public boolean addService(Object service) {
    var annotation = service.getClass().getAnnotation(Tinkle.class);
    if (annotation == null) {
      return false;
    }
    var serviceName = service.getClass().getCanonicalName();
    if (serviceNameSet.contains(serviceName)) {
      return true;
    }
    serviceMap.put(serviceName, service);
    if (publishService(service)) {
      serviceNameSet.add(serviceName);
    }
    return true;
  }

  @Override
  public Object getService(ServiceProperties serviceProperties) {
    return serviceMap.get(serviceProperties.toString());
  }

  @Override
  public boolean publishService(Object service) {
    String host;
    try {
      host = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      logger.error("getHostAddress error before publishService. service:{}.", service.getClass().getCanonicalName(), e);
      return false;
    }
    var annotation = service.getClass().getAnnotation(Tinkle.class);
    if (annotation == null) {
      return false;
    }
    this.addService(service);
    return registerService.register(service, new InetSocketAddress(host, CommonConstants.TINKLE_RPC_PORT));
  }

}
