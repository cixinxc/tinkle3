package cn.cixinxc.tinkle.common.utils;

import cn.cixinxc.tinkle.common.model.ServiceProperties;

import java.util.Optional;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/19
 */
public class ServicePropertiesUtils {
  /**
   * get service properties(canonicalName as serviceName, version and group)
   *
   * @param service service object
   */
  public static Optional<ServiceProperties> getServiceProperties(Object service) {
    return Optional.ofNullable(ServiceProperties.transToProperties(service));
  }

  /**
   * get service remote path -> {serviceName}:{version}:{groupName}
   *
   * @param service service object
   */
  public static String getServiceRemotePath(Object service) {
    return getServiceProperties(service).orElseGet(() -> ServiceProperties.EMPTY_PROPERTIES).toString();
  }
}
