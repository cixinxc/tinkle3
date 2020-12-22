package cn.cixinxc.tinkle.service;


import cn.cixinxc.tinkle.common.model.ServiceProperties;

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
  Object getService(ServiceProperties properties);

  /**
   * publish service
   *
   * @param service service object
   */
  boolean publishService(Object service);

}
