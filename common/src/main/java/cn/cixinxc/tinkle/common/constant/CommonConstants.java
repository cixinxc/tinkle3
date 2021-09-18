package cn.cixinxc.tinkle.common.constant;

import cn.cixinxc.tinkle.common.utils.FilePropertiesUtils;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/23
 */
public class CommonConstants {
  public static final int TINKLE_RPC_PORT = Integer.parseInt(FilePropertiesUtils.getValue("server.port", "9090"));
  public static final String ZK_REGISTER_ROOT_PATH = FilePropertiesUtils.getValue("zookeeper.register.root.path", "/TINKLE");
  public static final String DEFAULT_ZOOKEEPER_ADDRESS = FilePropertiesUtils.getValue("zookeeper.address", "127.0.0.1:2181");

  public static final byte[] MAGIC_NUMBER = {(byte) 'T', (byte) 'I', (byte) 'N', (byte) 'K'};

}
