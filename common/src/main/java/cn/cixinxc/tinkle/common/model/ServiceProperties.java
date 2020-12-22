package cn.cixinxc.tinkle.common.model;

import cn.cixinxc.tinkle.common.annotation.Tinkle;
import cn.cixinxc.tinkle.common.annotation.TinkleClient;

public class ServiceProperties {

  /**
   * service version
   */
  private String version;
  /**
   * service group
   */
  private String groupName;

  /**
   * fully qualified name of the class
   */
  private String serviceName;

  public ServiceProperties() {
  }

  public ServiceProperties(String serviceName, String version, String groupName) {
    this.version = version;
    this.groupName = groupName;
    this.serviceName = serviceName;
  }

  public ServiceProperties(TinkleClient annotation, String serviceName) {
    this.version = annotation.version();
    this.groupName = annotation.group();
    this.serviceName = serviceName;
  }

  public ServiceProperties(Tinkle annotation, String serviceName) {
    this.version = annotation.version();
    this.groupName = annotation.group();
    this.serviceName = serviceName;
  }

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

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  /**
   * {serviceName}:{version}:{groupName}
   */
  @Override
  public String toString() {
    return serviceName + "" + version + "" + groupName;
  }

}
