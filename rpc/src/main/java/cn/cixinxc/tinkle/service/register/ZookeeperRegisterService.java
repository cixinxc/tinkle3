package cn.cixinxc.tinkle.service.register;

import cn.cixinxc.tinkle.common.model.ServiceProperties;
import cn.cixinxc.tinkle.common.utils.CuratorUtils;
import cn.cixinxc.tinkle.common.utils.ServicePropertiesUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.InetSocketAddress;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/19
 */
public class ZookeeperRegisterService implements RegisterService {

  private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public boolean register(Object service, InetSocketAddress address) {
    var getServiceRemotePath = ServicePropertiesUtils.getServiceRemotePath(service);
    if (StringUtils.isBlank(getServiceRemotePath)) {
      return false;
    }
    var servicePath = CuratorUtils.ZK_REGISTER_ROOT_PATH + "/"
            + getServiceRemotePath
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
    // todo
    // var socketAddressArray = serviceUrls.get(new Random(serviceUrls.size()).nextInt(serviceUrls.size() + 1)).split(":");
    var socketAddressArray = serviceUrls.get(0).split(":");
    return new InetSocketAddress(socketAddressArray[0], Integer.parseInt(socketAddressArray[1]));
  }

}
