package cn.cixinxc.tinkle.register;

import cn.cixinxc.tinkle.common.model.ServiceProperties;
import cn.cixinxc.tinkle.common.utils.CuratorUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Random;

import static cn.cixinxc.tinkle.common.utils.ServicePropertiesUtils.getServiceRemotePath;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/19
 */
public class ZookeeperRegisterService implements RegisterService {

  private static final Logger logger = LoggerFactory.getLogger(ZookeeperRegisterService.class);

  @Override
  public boolean register(Object service, InetSocketAddress address) {
    var servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH
            + getServiceRemotePath(service) + "/"
            + address.toString();
    return CuratorUtils.createNode(CuratorUtils.getInstance(), servicePath);
  }

  @Override
  public InetSocketAddress lookup(ServiceProperties properties) {
    var zkFramework = CuratorUtils.getInstance();
    var serviceUrls = CuratorUtils.listChildNodes(zkFramework, properties.toString());
    if (CollectionUtils.isEmpty(serviceUrls)) {
      return null;
    }
    var socketAddressArray = serviceUrls.get(new Random(serviceUrls.size()).nextInt()).split(":");
    return new InetSocketAddress(socketAddressArray[0], Integer.parseInt(socketAddressArray[1]));
  }

}
