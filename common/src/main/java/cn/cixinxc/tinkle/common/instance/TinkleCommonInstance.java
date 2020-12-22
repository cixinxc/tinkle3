package cn.cixinxc.tinkle.common.instance;

import cn.cixinxc.tinkle.common.utils.PropertiesFileUtils;

/**
 * @author Cui Xinxin
 * @createDate 2020/12/23
 */
public class TinkleCommonInstance {
  public static final int TINKLE_RPC_PORT = Integer.parseInt(PropertiesFileUtils.getValue("server.port", "6358"));
}
