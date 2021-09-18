package cn.cixinxc.tinkle.service.register;

import cn.cixinxc.tinkle.common.model.ServiceProperty;

import java.net.InetSocketAddress;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/19
 */
public interface RegisterService {

  /**
   * register service
   *
   * @param service service
   * @param address service's address
   */
  boolean register(Object service, InetSocketAddress address);

  /**
   * lookup service address from register center
   *
   * @param properties service properties
   */
  InetSocketAddress lookup(ServiceProperty properties);
}
