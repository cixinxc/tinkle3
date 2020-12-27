package cn.cixinxc.tinkle.netty.server;

import cn.cixinxc.tinkle.common.utils.CuratorUtils;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/27
 */
public class CustomShutdownHook {
  private static final CustomShutdownHook CUSTOM_SHUTDOWN_HOOK = new CustomShutdownHook();

  public static CustomShutdownHook getCustomShutdownHook() {
    return CUSTOM_SHUTDOWN_HOOK;
  }

  public void clearAll() {
//        log.info("addShutdownHook for clearAll");
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), 9090);
        CuratorUtils.clear(CuratorUtils.getInstance(), inetSocketAddress);
      } catch (UnknownHostException ignored) {
      }
      // todo kill all thread pool
    }));
  }
}
