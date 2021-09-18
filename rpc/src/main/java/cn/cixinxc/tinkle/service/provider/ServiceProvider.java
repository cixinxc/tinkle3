package cn.cixinxc.tinkle.service.provider;


import cn.cixinxc.tinkle.common.model.ServiceProperty;

public interface ServiceProvider {

  /**
   * add service
   *
   * @param service service object
   */
  boolean addService(Object service);

  /**
   * get service
   *
   * @param properties service properties
   */
  Object getService(ServiceProperty properties);

  /**
   * publish service
   *
   * @param service service object
   */
  boolean publishService(Object service);

}
