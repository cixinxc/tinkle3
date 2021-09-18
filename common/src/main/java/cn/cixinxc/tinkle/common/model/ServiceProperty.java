package cn.cixinxc.tinkle.common.model;

import cn.cixinxc.tinkle.common.annotation.Tinkle;
import cn.cixinxc.tinkle.common.annotation.TinkleClient;
import cn.cixinxc.tinkle.common.utils.ObjectUtils;

import java.io.Serializable;

public class ServiceProperty implements Serializable {

  public static ServiceProperty EMPTY_PROPERTY = new ServiceProperty();

  /**
   * service group
   */
  private String groupName;

  /**
   * fully qualified name of interface
   */
  private String serviceName;

  /**
   * service version
   */
  private String version;

  /**
   * non-parameter constructor
   */
  public ServiceProperty() {
  }

  /**
   * all-parameter constructor
   *
   * @param serviceName interface name
   * @param version     version
   * @param groupName   group
   */
  public ServiceProperty(String groupName, String serviceName, String version) {
    this.version = version;
    this.groupName = groupName;
    this.serviceName = serviceName;
  }

  /****************************************** Getter and Setter ******************************************/
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getGroupName() {
    return groupName;
  }

  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  public String getServiceName() {
    return this.serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  /**
   * {groupName}:{serviceName}:{version}
   */
  @Override
  public String toString() {
    if (this == EMPTY_PROPERTY) {
      return "";
    }
    return groupName + ":" + serviceName + ":" + version;
  }

  /****************************************** Tinkle/TinkleClient -> Properties******************************************/
  public static ServiceProperty transToTinkleClientProperty(TinkleClient annotation, Class<?> bean) {
    if (annotation == null || bean == null) {
      return EMPTY_PROPERTY;
    }
    return new ServiceProperty(annotation.group(), bean.getCanonicalName(), annotation.version());
  }

  public static ServiceProperty transToTinkleProperty(Object service) {
    if (service == null) {
      return EMPTY_PROPERTY;
    }
    Tinkle annotation = service.getClass().getAnnotation(Tinkle.class);
    if (annotation == null) {
      return EMPTY_PROPERTY;
    }
    return new ServiceProperty(annotation.group(), ObjectUtils.getFirstInterfaceNames(service), annotation.version());
  }
}
