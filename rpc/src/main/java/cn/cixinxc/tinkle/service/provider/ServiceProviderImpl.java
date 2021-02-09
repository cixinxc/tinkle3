package cn.cixinxc.tinkle.service.provider;


import cn.cixinxc.tinkle.common.instance.CommonConstants;
import cn.cixinxc.tinkle.common.model.ServiceProperties;
import cn.cixinxc.tinkle.common.utils.ObjectUtils;
import cn.cixinxc.tinkle.service.register.RegisterService;
import cn.cixinxc.tinkle.service.register.ZookeeperRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static cn.cixinxc.tinkle.common.model.ServiceProperties.EMPTY_PROPERTIES;


public class ServiceProviderImpl implements ServiceProvider {

  private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

  private static final int SERVICE_MAP_DEFAULT_CAPACITY = 4096;
  private static final Map<String, Object> SERVICE_MAP = new ConcurrentHashMap<>(SERVICE_MAP_DEFAULT_CAPACITY);
  private static final Set<String> SERVICE_NAME_SET = new HashSet<>(SERVICE_MAP_DEFAULT_CAPACITY);

  private static final ServiceProvider SERVICE_PROVIDER = new ServiceProviderImpl();

  private static final RegisterService REGISTER_SERVICE = new ZookeeperRegisterService();

  private ServiceProviderImpl() {
  }

  public static ServiceProvider getInstance() {
    return SERVICE_PROVIDER;
  }

  @Override
  public boolean addService(Object service) {
    ServiceProperties properties = ServiceProperties.transToProperties(service);
    if (properties == EMPTY_PROPERTIES) {
      return false;
    }
    if (SERVICE_NAME_SET.contains(properties.toString())) {
      return true;
    }
    SERVICE_MAP.put(properties.toString(), service);
    return true;
  }

  @Override
  public Object getService(ServiceProperties serviceProperties) {
    return SERVICE_MAP.get(serviceProperties.toString());
  }

  @Override
  public boolean publishService(Object service) {
    String host;
    try {
      host = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      logger.error("getHostAddress error before publishService. service:{}.", service, e);
      return false;
    }
    if (!this.addService(service)) {
      return false;
    }
    if (REGISTER_SERVICE.register(service, new InetSocketAddress(host, CommonConstants.TINKLE_RPC_PORT))) {
      SERVICE_NAME_SET.add(ObjectUtils.getFirstInterfaceNames(service));
      return true;
    }
    return false;
  }

}
