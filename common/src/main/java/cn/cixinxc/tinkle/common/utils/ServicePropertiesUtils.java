package cn.cixinxc.tinkle.common.utils;

import cn.cixinxc.tinkle.common.model.ServiceProperty;

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
  public static Optional<ServiceProperty> getServiceProperties(Object service) {
    return Optional.ofNullable(ServiceProperty.transToTinkleProperty(service));
  }

  /**
   * get service remote path -> {serviceName}:{version}:{groupName}
   *
   * @param service service object
   */
  public static String getServiceRemotePath(Object service) {
    return getServiceProperties(service).orElseGet(() -> ServiceProperty.EMPTY_PROPERTY).toString();
  }
}
