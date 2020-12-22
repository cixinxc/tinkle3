package cn.cixinxc.tinkle.common.utils;

import cn.cixinxc.tinkle.common.annotation.Tinkle;
import cn.cixinxc.tinkle.common.model.ServiceProperties;

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
  public static ServiceProperties getServiceProperties(Object service) {
    var annotation = service.getClass().getAnnotation(Tinkle.class);
    return annotation == null
            ? new ServiceProperties()
            : new ServiceProperties(annotation, service.getClass().getCanonicalName());
  }

  /**
   * get service remote path -> {serviceName}:{version}:{groupName}
   *
   * @param service service object
   */
  public static String getServiceRemotePath(Object service) {
    return getServiceProperties(service).toString();
  }
}
