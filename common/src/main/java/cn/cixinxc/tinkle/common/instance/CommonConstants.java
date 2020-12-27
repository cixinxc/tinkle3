package cn.cixinxc.tinkle.common.instance;

import cn.cixinxc.tinkle.common.utils.PropertiesFileUtils;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/23
 */
public class CommonConstants {
  public static final int TINKLE_RPC_PORT = Integer.parseInt(PropertiesFileUtils.getValue("server.port", "6358"));

  public static final byte[] MAGIC_NUMBER = {(byte) 'T', (byte) 'I', (byte) 'N', (byte) 'K', (byte) 'L', (byte) 'E'};
}
